package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.model.DrDeviceLegalCase;
import org.example.legalinformaticbackend.repository.DrDeviceLegalCaseRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DrDeviceLegalCaseService {
    private final DrDeviceLegalCaseRepository repository;

    private final String backendDirPath = System.getProperty("user.dir");
    private final String drDevicePath = Paths.get(backendDirPath, "..", "dr-device").toString();

    private final String startBatPath = Paths.get(drDevicePath, "start.bat").toString();
    private final String cleanBatPath = Paths.get(drDevicePath, "clean.bat").toString();
    private final String factsRDFPath = Paths.get(drDevicePath, "facts.rdf").toString();
    private final String exportRDFPath = Paths.get(drDevicePath, "export.rdf").toString();

    private final String[] charges = {
            "has_deforested_forest",
            "has_desolated_forest",
            "in_prohibited_land",
            "defendant_desolated_forest",
            "in_special_forest",
            "defendant_desolated_special_forest",
            "defendant_stole_forest",
            "defendant_stole_forest_level_2",
            "defendant_has_intention_to_steal_forest",
    };

    public DrDeviceLegalCase addDrDeviceLegalCase(DrDeviceLegalCase drDeviceLegalCase) { return repository.save(drDeviceLegalCase); }

    private void runProcess(String processPath) {
        try{
            ProcessBuilder pb = new ProcessBuilder(processPath);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            pb.directory(new File(processPath).getParentFile());

            Process p = pb.start();
            int exitCode = p.waitFor();
            System.out.println("Script exited with code " + exitCode);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runStartBat() {
        this.runProcess(this.startBatPath);
    }

    public void runCleanBat() {
        this.runProcess(this.cleanBatPath);
    }

    private String getRDFHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
                "        xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n" +
                "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n" +
                "        xmlns:lc=\"http://informatika.ftn.uns.ac.rs/legal-case.rdf#\">\n" +
                "    <lc:case rdf:about=\"http://informatika.ftn.uns.ac.rs/legal-case.rdf#case01\">\n\n";
    }

    private String getRDFFooter() {
        return "    </lc:case>\n" +
                "\n" +
                "</rdf:RDF>";
    }

    private <T> String makeRDFTag(String tagName, T tagValue) {
        return "\t\t<lc:" + tagName + ">" + tagValue.toString() + "</lc:" + tagName + ">\n";
    }

    private String getRDFBody(DrDeviceLegalCase legalCase) {
        return this.makeRDFTag("defendant", legalCase.getDefendant()) +
                this.makeRDFTag("name", "case " + legalCase.getCaseNumber()) +
                this.makeRDFTag("deforestation", legalCase.getDeforestation() ? "yes": "no") +
                this.makeRDFTag("desolation", legalCase.getDesolation() ? "yes": "no") +
                this.makeRDFTag("prohibited_land", legalCase.getProhibited_land() ? "yes": "no") +
                this.makeRDFTag("special_forest", legalCase.getSpecial_forest() ? "yes": "no") +
                this.makeRDFTag("cut_more_than_1_m3", legalCase.getWoodVolume() > 1 ? "yes": "no") +
                this.makeRDFTag("intention_to_sell", legalCase.getIntention_to_sell() ? "yes": "no") +
                this.makeRDFTag("cut_more_than_5_m3", legalCase.getWoodVolume() > 5 ? "yes": "no") +
                this.makeRDFTag("had_intention", legalCase.getHad_intention() ? "yes": "no");
    }

    public void writeFactsRDF(DrDeviceLegalCase legalCase) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.factsRDFPath));

            String rdf = this.getRDFHeader() + this.getRDFBody(legalCase) + this.getRDFFooter();

            writer.write(rdf);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for all but imprisonment and to_pay just check if string arr is not empty
    // if it isn't empty the defendant is defeasibly-proven-positive for that attr
    // for to_pay and imprisonment just check how much (to pay is in euro, imprisonment is in months always)
    private ArrayList<String> findExportedAttribute(String rdfContent, String attribute) {
        Pattern pattern = Pattern.compile("<export:" + attribute + ".*>(\\s+.*\\s+)+<\\/export:" + attribute + ">");
        Matcher matcher = pattern.matcher(rdfContent);

        ArrayList<String> retval = new ArrayList<String>();

        while(matcher.find()) {
            retval.add(matcher.group());
        }

        return retval;
    }

    // if all charges are empty skip this method
    private Integer getRDFValueForAttribute(String rdfContent) {
        Pattern pattern = Pattern.compile("<export:value>(\\d+)<\\/export:value>");
        Matcher matcher = pattern.matcher(rdfContent);

        while(matcher.find()) {
            return Integer.valueOf(matcher.group(1));
        }

        return 0;
    }

    private String prettifyVerdict(String verdict) {
        // meta
        verdict = verdict.replace(": defeasibly-proven-positive", "");
        verdict = verdict.replace("Defendant is not guilty on any charges", "Optuženi nije pronađen kriv ni po jednoj stavci");
        verdict = verdict.replace("Defendant to pay", "Optuženi da plati");
        verdict = verdict.replace("Defendant minimum prison time", "Minimalna zatvorska kazna za optuženog");
        verdict = verdict.replace("Defendant maximum prison time", "Maksimalna zatvorska kazna za optuženog");

        // charges
        verdict = verdict.replace("has_deforested_forest", "Optuženi je vršio seču ili krčio šumu");
        verdict = verdict.replace("has_desolated_forest", "Optuženi je oštetio ili na drugi način pustošio šumu");
        verdict = verdict.replace("in_prohibited_land", "Optuženi je u parku, drvoredu ili drugom mestu gde je zabranjena seča");
        verdict = verdict.replace("defendant_desolated_forest", "Optuženi je kriv za krivično delo postošenje šuma iz čl.323 st.1");
        verdict = verdict.replace("defendant_desolated_special_forest", "Optuženi je kriv za krivično delo postošenje šuma iz čl.323 st.2");
        verdict = verdict.replace("in_special_forest", "Optuženi je u zaštićenoj šumi, nacionalnom parku ili drugoj šumi sa posebnom namenom");
        verdict = verdict.replace("defendant_stole_forest_level_2", "Optuženi je kriv za krivično delo šumska krađa iz čl.324 st.2");
        verdict = verdict.replace("defendant_stole_forest", "Optuženi je kriv za krivično delo šumska krađa iz čl.324 st.1");
        verdict = verdict.replace("defendant_has_intention_to_steal_forest", "Optuženi je kriv za krivično delo šumska krađa iz čl.324 st.3");

        return verdict;
    }

    public String parseExportRDF() throws IOException {
        Path path = Path.of(this.exportRDFPath);
        String rdfContent = Files.readString(path);

        StringBuilder indictment = new StringBuilder();

        // check charges
        for(String attr: this.charges) {
            ArrayList<String> exportedCharges = this.findExportedAttribute(rdfContent, attr);

            if(exportedCharges.size() == 0) {
                continue;
            }

            indictment.append(attr).append(": defeasibly-proven-positive\n");
        }

        // if no charges found
        if(indictment.isEmpty()) { return "Defendant is not guilty on any charges"; }

        // if charges found check to_pay and imprisonment
        // to_pay
        ArrayList<String> payments = this.findExportedAttribute(rdfContent, "to_pay");
        ArrayList<Integer> paymentValues = new ArrayList<Integer>();

        if(!payments.isEmpty()) {
            for(String paymentBlock: payments) {
                paymentValues.add(this.getRDFValueForAttribute(paymentBlock));
            }
        }

        Integer toPay = Collections.max(paymentValues);

        // imprisonment
        ArrayList<String> imprisonments = this.findExportedAttribute(rdfContent, "imprisonment");
        ArrayList<Integer> imprisonmentValues = new ArrayList<Integer>();

        if(!imprisonments.isEmpty()) {
            for(String imprisonmentsBlock: imprisonments) {
                imprisonmentValues.add(this.getRDFValueForAttribute(imprisonmentsBlock));
            }
        }

        Integer minPrisonTime = 0;
        Integer maxPrisonTime = 0;
        if(imprisonmentValues.size() == 1) {
            minPrisonTime = 0;
        }
        else {
            minPrisonTime = Collections.min(imprisonmentValues);
        }
        maxPrisonTime = Collections.max(imprisonmentValues);

        indictment.append("Defendant to pay: ").append(toPay.toString()).append("€\n");
        indictment.append("Defendant minimum prison time: ").append(minPrisonTime.toString()).append(" meseci\n");
        indictment.append("Defendant maximum prison time: ").append(maxPrisonTime.toString()).append(" meseci\n");

        return this.prettifyVerdict(indictment.toString());
    }

    public String getVerdictArticles(String prettyVerdict) {
        ArrayList<String> articles = new ArrayList<String>();

        Pattern pattern = Pattern.compile("čl.(323|324)\\s?st.\\d{1,2}");
        Matcher matcher = pattern.matcher(prettyVerdict);

        while (matcher.find()) {
            articles.add(matcher.group());
        }

        StringBuilder retVal = new StringBuilder();

        for (String article: articles) {
            retVal.append(article);
            retVal.append(", ");
        }

        return retVal.toString();
    }

    private String getSingleGroupMatch(String source, Pattern pattern) {
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            return matcher.group();
        }

        return "";
    }

    public String getVerdictPenalties(String prettyVerdict) {
        String paymentPenalty = this.getSingleGroupMatch(prettyVerdict, Pattern.compile("optuženi da plati: \\d+"));
        String minImprisonment = this.getSingleGroupMatch(prettyVerdict, Pattern.compile("minimalna zatvorska kazna za optuženog: \\d+"));
        String maxImprisonment = this.getSingleGroupMatch(prettyVerdict, Pattern.compile("maksimalna zatvorska kazna za optuženog: \\d+"));

        return paymentPenalty + ", " + minImprisonment + ", " + maxImprisonment;
    }
}
