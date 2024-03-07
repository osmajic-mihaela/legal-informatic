package org.example.legalinformaticbackend.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.repository.LegalCaseRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AttributeExtractionService {
    private final LegalCaseRepository legalCaseRepository;
    private final ResourceLoader resourceLoader;

    public Map attributeExtraction(String caseNumber){
        Map<String, String> retVal = new HashMap<>();

        try {
            String caseStr = this.readPDF(caseNumber);
            retVal.put("Broj slučaja", extractCaseNumber(caseStr));
            retVal.put("Sud", extractCourt(caseStr));
            retVal.put("Sudija", extractJudgeName(caseStr));
            retVal.put("Zapisničar", extractCourtReporterName(caseStr));
            retVal.put("Optuženi", extractDefendantInitials(caseStr));
            retVal.put("Šumska svojina", extractForestProperty(caseStr));
            retVal.put("Novčana šteta", extractFinancialDamage(caseStr));
            retVal.put("Kubna drvna masa", extractWoodVolume(caseStr));
            retVal.put("Svestan", extractAwareness(caseStr));
            retVal.put("Osuđen", isConvicted(caseStr));
            retVal.put("Broj stabala", extractNumberOfTrees(caseStr));
            retVal.put("Vrsta drveta", extractTreeType(caseStr));
            retVal.put("Razlog presude", extractReasonForProsecution(caseStr));
            //retVal.put("Članovi zakona", extractTreeType(caseStr));

            if(retVal.get("Osuđen").equals("Da")){
                retVal.put("Uslovna", isConditionalSentence(caseStr));
                retVal.put("Zatvorska kazna", extractPrisonSentence(caseStr));
                retVal.put("Novčana kazna", extractFinancialSentence(caseStr));
                retVal.put("Kazna rada u javnom interesu", extractComunityServiceSentence(caseStr));


            }else{
                retVal.put("Uslovna", "");
                retVal.put("Zatvorska kazna", "");
                retVal.put("Novčana kazna",  "");
                retVal.put("Kazna rada u javnom interesu", "");
            }



            //Vremenski rok za placanje?
            //zamena ako ne plati?


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }


    private String readPDF(String caseNumber) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:cases/" + caseNumber + ".pdf");
        Path path = Paths.get(resource.getURI());
        File file = new File(path.toString());
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }


    //radi za sve slucajeve
    private String extractCaseNumber(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("^[kK]\\.\\s?([Bb]r\\.\\s?)?[0-9]{1,4}/[0-9]{2}\\s");
        Matcher matcher = pattern.matcher(caseStr);
        String ret = " ";
        if (matcher.find()) {
            ret = matcher.group();
        } else {
            Pattern pattern2 = Pattern.compile("\\s[kK]\\.\\s?([Bb]r\\.\\s?)?[0-9]{1,4}/[0-9]{2}\\s");
            Matcher matcher2 = pattern2.matcher(caseStr);
            if (matcher2.find()) {
                ret = matcher2.group();
            }
        }
        return ret.trim();
    }


    //radi za sve slucajeve
    private String extractCourt(String caseStr) throws IOException {
        Pattern pattern = Pattern.compile("\\s((U IME CRNE GORE)|(U IME NARODA))\\s*[A-ZŽĐŠČĆa-zžđšćčć]+ ((SUD U)|(sud u)) [A-ZŽĐŠČĆa-zžđšćčć]+");
        Matcher matcher = pattern.matcher(caseStr);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group();

            Pattern pattern2 = Pattern.compile("\\s[A-ZŽĐŠČĆa-zžđšćčć]+ ((SUD U)|(sud u)) [A-ZŽĐŠČĆa-zžđšćčć]+");
            Matcher matcher2 = pattern2.matcher(ret);
            if (matcher2.find()) {
                ret = matcher2.group();
            }
        }
        return ret.trim();
    }

    //radi za sve slucajeve
    public String extractJudgeName(String caseStr) throws IOException {
        Pattern pattern1 = Pattern.compile("(?:prvostepeni krivični sud)?\\s*(?:sudija|sudiji|sudiji pojedincu|sudija pojedincu|sudija pojedinac)\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+(?:\\s+[A-ZČĆĐŠŽ][a-zčćđšž]+)*\\s*[A-ZČĆĐŠŽ][a-zčćđšž]+(?:\\s*-?\\s*[A-ZČĆĐŠŽ][a-zčćđšž]+)?)");
        Matcher matcher1 = pattern1.matcher(caseStr);
        String name = "unknown";
        if (matcher1.find()) {
            name = matcher1.group(1);
            name = name.trim().replaceAll("[-,]", "");
            return name;
        }


        Pattern pattern2 = Pattern.compile("\\b([A-ZČĆĐŠŽ][a-zčćđšž]+(?:\\s+[A-ZČĆĐŠŽ][a-zčćđšž]+)*\\s*[A-ZČĆĐŠŽ][a-zčćđšž]+(?:\\s*-?\\s*[A-ZČĆĐŠŽ][a-zčćđšž]+)?)");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            name = matcher2.group(1);
            name = name.trim().replaceAll("[-,]", "");
            return name;
        }

        return name;
    }

    //radi za sve, sredi izlaz
    private String extractDefendantInitials(String caseStr) throws IOException {
        Pattern pattern1 = Pattern.compile("\\b([A-ZŽĐŠČĆ]\\.[A-ZŽĐŠČĆ]\\.)(?=\\s|$)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            return removeWhitespace(removeLowercase(matcher1.group()));
        }

        Pattern pattern2 = Pattern.compile("(?:okrivljenog|optuženog)\\s+([A-ZŽĐŠČĆ]\\.\\s?[A-ZŽĐŠČĆ](?:\\.[a-zčćđšžA-ZŽĐŠČĆ])?)\\b");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            return removeWhitespace(removeLowercase(matcher2.group(1)));
        }

        Pattern pattern3 = Pattern.compile("\\b([A-ZŽĐŠČĆ]\\.[a-zčćđšžA-ZŽĐŠČĆ]?\\s?[A-Z]\\.[a-zčćđšžA-ZŽĐŠČĆ]?\\.?)\\b");
        Matcher matcher3 = pattern3.matcher(caseStr);

        if (matcher3.find()) {
            return removeWhitespace(removeLowercase(matcher3.group(1)));
        }


        return "unknown";
    }

    private static String removeLowercase(String text) {
        return text.replaceAll("[a-z]", "");
    }

    private static String removeWhitespace(String text) {
        return text.replaceAll(" ", "");
    }

    //radi za sve
    private String extractCourtReporterName(String caseStr) throws IOException {
        Pattern pattern = Pattern.compile("[zZ]apisničar(?:a|om|em)?\\s+([A-ZŽĐŠČĆ][a-zčćđšžA-ZŽĐŠČĆ]+(?:\\s+[A-ZŽĐŠČĆ][a-zčćđšžA-ZŽĐŠČĆ]+)?)(,)?\\s*(kao)?\\s*(zapisničara)?");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return matcher.group(1).replace("\r\n", " ").replace("\n", " ").trim();
        }

        Pattern pattern2 = Pattern.compile("([A-ZŽĐŠČĆ][a-zčćđšžA-ZŽĐŠČĆ]+(?:\\s+[A-ZŽĐŠČĆ][a-zčćđšžA-ZŽĐŠČĆ]+)?)(,\\s+)?(kao)?\\s+zapisničara(,)?");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            return matcher2.group(1).replace("\r\n", " ").replace("\n", " ").trim();
        }

        return "unknown";

    }

    //radi za sve
    private String extractFinancialDamage(String caseStr) throws IOException {
        Pattern pattern1 = Pattern.compile("((kaznu|penzije)\\s*u\\s*iznosu\\s*od)\\s*(\\d{1,7}(?:\\.\\d{3,6})*(?:,\\d{2,5})?)\\s*(?:eura|€)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            caseStr = matcher1.replaceAll("");
        }


        Pattern pattern2 = Pattern.compile("(iznosu\\s*od)\\s*(\\d{1,7}(?:\\.\\d{3,6})*(?:,\\d{2,5})?)\\s*(?:eura|€)");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            //String s =  matcher2.group(2).replace(".", "");
            //return Double.parseDouble(s.replace(',', '.'));
            return matcher2.group(2).replace(".", "").replace(',', '.');
        }

        //return 0.0;
        return "unknown";

    }

    //radi za sve slucajeve
    private String extractForestProperty(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("((privatnoj\\s*šumi)|(vlasništvo\\s*oštećenih))");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return "Privatna";
        }
        return "Državna";
    }

    //radi za sve slucajeve
    private String extractAwareness(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("svjestan\\s*(zabranjenosti\\s*)?svog\\s*djela");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return "Da";
        }
        return "Ne";
    }

    //radi za sve slucajeve
    private String isConvicted(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("[kK]\\s*[rR]\\s*[iI]\\s*[vV]\\s*[jJ]\\s*[eE]");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return "Da";
        }
        return "Ne";
    }

    //radi za sve
    private String extractWoodVolume(String caseStr) {
        //Pattern pattern1 = Pattern.compile("u\\s*ukupnoj\\s*(koli[čc]ini|(bruto|kubnoj)\\s*masi)\\s*(od\\s*)?(\\d+\\,\\d+)");
        Pattern pattern1 = Pattern.compile("(koli[čc]ini|mas(?:i|e))\\s*(od\\s*)?(\\d+\\,\\d+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String numberStr = matcher1.group(3);
            //return Double.parseDouble(numberStr.replace(',', '.'));
            return numberStr.replace(',', '.');
        }
        //return 0.0;

        return "unknown";
    }

    //U K114/19 i K116/19 dve vrste stabla su u pitanju, pa se navodi br za svaku vrstu, ne izvlaci tu drugu
    private String extractNumberOfTrees(String caseStr) {
        Pattern pattern1 = Pattern.compile("oborio\\s*(u\\s*državnoj\\s*šumi\\s*)?(\\d+|jedno)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String br = matcher1.group(2);
            if (br.equals("jedno")) {
                return "1";
            } else {
                return br;
            }
        }

        return "unknown";

    }

    // K114/19 i K116/19
    private String extractTreeType(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("([a-zčćđšžA-ZŽĐŠČĆ]+)\\s*stab(a)?l(a|o)");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            String type = matcher.group(1);
            if(!type.contains("buk") && !type.contains("hra") && !type.contains("smr") && !type.contains("čam") && !type.contains("grab")){
                Pattern pattern1 = Pattern.compile("stab(a)?l(a|o)\\s*([a-zčćđšžA-ZŽĐŠČĆ]+)");
                Matcher matcher1 = pattern1.matcher(caseStr);
                if (matcher1.find()) {
                    return matcher1.group(3);
                }
            }
            return type;
        }
        return "unknown";
    }

    //radi za sve
    private String isConditionalSentence(String caseStr) {
        Pattern pattern = Pattern.compile("USLOVNU\\s*OSUDU");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return "Da";
        }
        return "Ne";
    }

    //radi za sve
    private String extractFinancialSentence(String caseStr) {
        Pattern pattern1 = Pattern.compile("nov[cč]anu\\s*kaznu\\s*u\\s*iznosu\\s*od\\s*(\\d+\\,\\d+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        Pattern pattern2 = Pattern.compile("[kK]\\s*[rR]\\s*[iI]\\s*[vV]\\s*[jJ]\\s*[eE]");
        Matcher matcher2 = pattern2.matcher(caseStr);
        int startIndex2 = 0;
        if(matcher2.find()){
            startIndex2 = matcher2.start();
        }

        while (matcher1.find()) {
            int startIndex1 = matcher1.start();
            if(startIndex1 > startIndex2){
                String numberStr = matcher1.group(1);
                //return Double.parseDouble(numberStr.replace(',', '.'));
                return numberStr.replace(',', '.');
            }

        }
        //return 0.0;

        return "unknown";

    }

    private String extractPrisonSentence(String caseStr) {
        Pattern pattern1 = Pattern.compile("kaznu\\s*zatvora\\s*(u\\s*trajanju)?\\s*od\\s*(\\d+)\\s*(?:\\(\\s*[a-zčćđšžA-ZŽĐŠČĆ\\s*]+\\s*\\))?\\s*(?:\\/\\s*([a-zčćđšžA-ZŽĐŠČĆ\\s*]+)\\s*\\/)?\\s*([a-zčćđšžA-ZŽĐŠČĆ]+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String numberStr = matcher1.group(2);
            String periodStr = matcher1.group(4);
            //return Double.parseDouble(numberStr.replace(',', '.'));
            return numberStr+" "+periodStr;
        }
        //return 0.0;

        return "unknown";

    }

    private String extractComunityServiceSentence(String caseStr) {
        Pattern pattern1 = Pattern.compile("kaznu\\s*rada\\s*u\\s*javnom\\s*interesu\\s*(u\\s*trajanju)?\\s*od\\s*(\\d+)\\s*(?:\\(\\s*[a-zčćđšžA-ZŽĐŠČĆ\\s*]+\\s*\\))?\\s*(?:\\/\\s*([a-zčćđšžA-ZŽĐŠČĆ\\s*]+)\\s*\\/)?\\s*([a-zčćđšžA-ZŽĐŠČĆ]+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String numberStr = matcher1.group(2);
            String periodStr = matcher1.group(4);
            //return Double.parseDouble(numberStr.replace(',', '.'));
            return numberStr+" "+periodStr;
        }
        //return 0.0;

        return "unknown";

    }


    //radi za sve, da li treba i deo u vezi clana??
    private String extractReasonForProsecution(String caseStr) {

        Pattern pattern1 = Pattern.compile("izvršio\\s*krivično\\s*djelo\\s*((šumska)?\\s*kra[djđ]+a|pusto[šs]enje\\s*šuma)\\s*iz\\s*čl(\\.)?\\s*(\\d+)\\s*(\\.)?\\s*st\\s*(\\.)?\\s*(\\d+)\\s*(\\.)?\\s*(u\\s*vezi\\s*st\\s*(\\.)?\\s*(\\d+))?");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String clan = matcher1.group(4);
            String stav = matcher1.group(7);

            //return Double.parseDouble(numberStr.replace(',', '.'));
            return clan+" "+stav;
        }
        //return 0.0;

        return "unknown";
    }





    /*

    POKUSAJ RESAVANJA EKSTRAKCIJE BROJA STABALA PRESUDA SA 2 VRSTE DRVETA

    private String extractNumberOfTrees2(String caseStr) {
        Pattern pattern1 = Pattern.compile("oborio\\s*(u\\s*državnoj\\s*šumi\\s*)?(\\d+|jedno)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if(matcher1.find()){
            return getAllMatchedPatternsForWoodVolume(matcher1,2);
        }

        Pattern pattern2 = Pattern.compile("(\\d+|jedno)\\s*([\\)\\(a-zčćđšžA-ZŽĐŠČĆ]*\\s*)stabl(?:o|a)");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if(matcher2.find()){
            return getAllMatchedPatternsForWoodVolume(matcher2,1);
        }

        return  "unknown";
    }

    private String getAllMatchedPatternsForNumberOfTrees(Matcher matcher, int group){
        List<String> resultList = new ArrayList<>();
        while (matcher.find()) {
            String br = matcher.group(group);
            if (br.equals("jedno")) {
                resultList.add("1");
            } else {
                resultList.add(br);
            }
        }

        if(resultList.size() == 1){
            return resultList.get(0);
        }

        return String.join(",", resultList);

    }*/

}
