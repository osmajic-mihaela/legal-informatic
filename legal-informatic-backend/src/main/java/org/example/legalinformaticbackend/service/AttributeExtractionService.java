package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.example.legalinformaticbackend.model.DbEntity;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.repository.LegalCaseRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AttributeExtractionService {
    private final LegalCaseRepository legalCaseRepository;
    private final ResourceLoader resourceLoader;



    //Vremenski rok za placanje?
    //Zamena ako ne plati?
    public DbEntity attributeExtraction(String caseNumber){
        Map<String, String> retVal = new HashMap<>();
        LegalCase legalCase = new LegalCase();

        try {
            String caseStr = this.readPDF(caseNumber);

            /*retVal.put("Broj slučaja",extractCaseNumber(caseStr) );
            retVal.put("Sud", extractCourt(caseStr));
            retVal.put("Sudija", extractJudgeName(caseStr));
            retVal.put("Zapisničar", extractCourtReporterName(caseStr));
            retVal.put("Optuženi", extractDefendantInitials(caseStr));
            retVal.put("Zaštićena površina", extractProtectedSurface(caseStr));

            retVal.put("Šumska svojina", extractForestProperty(caseStr));
            retVal.put("Novčana šteta", extractFinancialDamage(caseStr));
            retVal.put("Kubna drvna masa", extractWoodVolume(caseStr));
            retVal.put("Svestan", extractAwareness(caseStr));
            retVal.put("Osuđen", isConvicted(caseStr));
            retVal.put("Broj stabala", extractNumberOfTrees(caseStr)); //Ne radi za 2 vrste stabala
            retVal.put("Vrsta drveta", extractTreeType(caseStr)); //Ne radi za 2 vrste stabala
            retVal.put("Razlog presude", extractReasonForProsecution(caseStr)); //Da li treba u vezi clana?


            if(retVal.get("Osuđen").equals("Da")){
                retVal.put("Uslovna", isConditionalSentence(caseStr));
                retVal.put("Zatvorska kazna", extractPrisonSentence(caseStr));
                retVal.put("Novčana kazna", extractFinancialSentence(caseStr));
                retVal.put("Kazna rada u javnom interesu", extractComunityServiceSentence(caseStr));
                retVal.put("Citirani članovi zakona", extractCitedArticles(caseStr));

            }else{
                retVal.put("Uslovna", "");
                retVal.put("Zatvorska kazna", "");
                retVal.put("Novčana kazna",  "");
                retVal.put("Kazna rada u javnom interesu", "");
                retVal.put("Citirani članovi zakona", "");
            }*/

            legalCase.setCaseNumber(extractCaseNumber(caseStr));
            legalCase.setCourt(extractCourt(caseStr));
            legalCase.setJudge(extractJudgeName(caseStr));
            legalCase.setCourtReporter(extractCourtReporterName(caseStr));
            legalCase.setDefendant(extractDefendantInitials(caseStr));
            legalCase.setProtectedSurface(extractProtectedSurface(caseStr));

            legalCase.setForestProperty(extractForestProperty(caseStr));
            legalCase.setFinancialDamage(extractFinancialDamage(caseStr));
            legalCase.setWoodVolume(extractWoodVolume(caseStr));
            legalCase.setAwareness(extractAwareness(caseStr));
            legalCase.setConvicted(isConvicted(caseStr));
            legalCase.setNumberOfTrees(extractNumberOfTrees(caseStr));
            legalCase.setTreeType(extractTreeType(caseStr));
            legalCase.setReasonForProsecution(extractReasonForProsecution(caseStr));

            //za boolean vrednosti
            if (legalCase.getConvicted()) {
                legalCase.setConditionalSentence(isConditionalSentence(caseStr));
                legalCase.setPrisonSentence(extractPrisonSentence(caseStr));
                legalCase.setFinancialSentence(extractFinancialSentence(caseStr));
                legalCase.setCommunitySentence(extractComunityServiceSentence(caseStr));
                legalCase.setCitedArticles(extractCitedArticles(caseStr));
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        return legalCase;
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

        Pattern pattern = Pattern.compile("([Kk])\\.\\s*([Bb]r\\.\\s*)?(\\d+/\\d+)");
        Matcher matcher = pattern.matcher(caseStr);
        String ret = "unknown";
        if (matcher.find()) {
            ret =matcher.group(1)+" "+matcher.group(3);
        }

        return ret.trim();
    }


    //radi za sve slucajeve
    private String extractCourt(String caseStr) throws IOException {
        //Pattern pattern = Pattern.compile("\\s((U IME CRNE GORE)|(U IME NARODA))\\s*[A-ZŽĐŠČĆa-zžđšćčć]+\\s*((SUD\\s*U)|(sud\\s*u))\\s*[A-ZŽĐŠČĆa-zžđšćčć]+");
        Pattern pattern = Pattern.compile("(U\\s*I\\s*M\\s*E\\s*C\\s*R\\s*N\\s*E\\s*G\\s*O\\s*R\\s*E|U\\s*I\\s*M\\s*E\\s*N\\s*A\\s*R\\s*O\\s*D\\s*A)\\s*([A-ZŽĐŠČĆa-zžđšćčć]+\\s*((SUD\\s*U)|(sud\\s*u))\\s*[A-ZŽĐŠČĆa-zžđšćčć]+)");
        Matcher matcher = pattern.matcher(caseStr);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group(2);
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

        Pattern pattern2 = Pattern.compile("([A-ZŽĐŠČĆ][a-zčćđšžA-ZŽĐŠČĆ]+\\s*[A-ZŽĐŠČĆ][a-zčćđšžA-ZŽĐŠČĆ]+)\\s*(,)?\\s*(kao)?\\s*zapisničara\\s*(,)?");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            return matcher2.group(1).replace("\r\n", " ").replace("\n", " ").trim();
        }

        return "unknown";

    }

    //radi za sve
    private Double extractFinancialDamage(String caseStr) throws IOException {
        Pattern pattern1 = Pattern.compile("((kaznu|penzije)\\s*u\\s*iznosu\\s*od)\\s*(\\d{1,7}(?:\\.\\d{3,6})*(?:,\\d{2,5})?)\\s*(?:eura|€)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            caseStr = matcher1.replaceAll("");
        }


        Pattern pattern2 = Pattern.compile("(iznosu\\s*od)\\s*(\\d{1,7}(?:\\.\\d{3,6})*(?:,\\d{2,5})?)\\s*(?:eura|€)");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            String s =  matcher2.group(2).replace(".", "");
            return Double.parseDouble(s.replace(',', '.'));
            //return matcher2.group(2).replace(".", "").replace(',', '.');
        }

        return 0.0;
        //return "unknown";

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
    private Boolean extractAwareness(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("svjestan\\s*(zabranjenosti\\s*)?svog\\s*djela");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return Boolean.TRUE;
        }

        Pattern pattern2 = Pattern.compile("svjesno\\s*i\\s*voljno");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //radi za sve slucajeve
    private Boolean isConvicted(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("[kK]\\s*[rR]\\s*[iI]\\s*[vV]\\s*([iI]\\s*)?([jJ]\\s*[eE]|[Ss]\\s*[Uu])");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //radi za sve
    private Double extractWoodVolume(String caseStr) {
        //Pattern pattern1 = Pattern.compile("u\\s*ukupnoj\\s*(koli[čc]ini|(bruto|kubnoj)\\s*masi)\\s*(od\\s*)?(\\d+\\,\\d+)");
        Pattern pattern1 = Pattern.compile("(koli[čc]ini|mas(?:i|e))\\s*(od\\s*)?(\\d+\\,\\d+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String numberStr = matcher1.group(3);
            return Double.parseDouble(numberStr.replace(',', '.'));
            //return numberStr.replace(',', '.');
        }
        return 0.0;

        //return "unknown";
    }

    //U K114/19 i K116/19 dve vrste stabla su u pitanju, pa se navodi br za svaku vrstu, ne izvlaci tu drugu
    private Integer extractNumberOfTrees(String caseStr) {
        Pattern pattern1 = Pattern.compile("obori(o|li)\\s*(u\\s*državnoj\\s*šumi\\s*|ukupno\\s*)?(\\d+|jedno)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String br = matcher1.group(3);
            if (br.equals("jedno")) {
                return 1;
            } else {
                return Integer.parseInt( br);
            }
        }

        Pattern pattern2 = Pattern.compile("(\\d+|jedno)\\s*stab(a)?l(a|o)");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            String br = matcher2.group(1);
            if (br.equals("jedno")) {
                return 1;
            } else {
                return Integer.parseInt( br);
            }
        }



        return 0;

    }

    // K114/19 i K116/19 - za dve vrste stabla
    private String extractTreeType(String caseStr) throws IOException {

        Pattern pattern = Pattern.compile("([a-zčćđšžA-ZŽĐŠČĆ]+)\\s*stab(a)?l(a|o)");
        Matcher matcher = pattern.matcher(caseStr);

        while (matcher.find()) {
            String type = matcher.group(1);
            if(!type.contains("buk") && !type.contains("hrast") && !type.contains("smr") && !type.contains("čam") && !type.contains("grab") && !type.contains("topol")){
                Pattern pattern1 = Pattern.compile("stab(a)?l(a|o)\\s*([a-zčćđšžA-ZŽĐŠČĆ]+)");
                Matcher matcher1 = pattern1.matcher(caseStr);
                while (matcher1.find()) {
                    String type1  = matcher1.group(3);
                    if(type1.contains("buk") || type1.contains("hrast") || type1.contains("smr") || type1.contains("čam") || type1.contains("grab") || type1.contains("topol")){
                        return type1;
                    }
                }
            }
            if(type.contains("buk") || type.contains("hrast") || type.contains("smr") || type.contains("čam") || type.contains("grab") || type.contains("topol")) {

                return type;
            }
        }
        return "unknown";
    }

    //radi za sve
    private Boolean isConditionalSentence(String caseStr) {
        Pattern pattern = Pattern.compile("U\\s*S\\s*L\\s*O\\s*V\\s*N\\s*U\\s*O\\s*S\\s*U\\s*D\\s*U");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //radi za sve
    private Double extractFinancialSentence(String caseStr) {
        Pattern pattern1 = Pattern.compile("nov[cč]anu\\s*kaznu\\s*u\\s*iznosu\\s*od\\s*(\\d+\\,\\d+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        Pattern pattern2 = Pattern.compile("[kK]\\s*[rR]\\s*[iI]\\s*[vV]\\s*([iI]\\s*)?([jJ]\\s*[eE]|[Ss]\\s*[Uu])");
        Matcher matcher2 = pattern2.matcher(caseStr);
        int startIndex2 = 0;
        if(matcher2.find()){
            startIndex2 = matcher2.start();
        }

        while (matcher1.find()) {
            int startIndex1 = matcher1.start();
            if(startIndex1 > startIndex2){
                String numberStr = matcher1.group(1);
                return Double.parseDouble(numberStr.replace(',', '.'));
                //return numberStr.replace(',', '.');
            }

        }
        return 0.0;

        //return "unknown";

    }

    //radi za sve
    private String extractPrisonSentence(String caseStr) {
        Pattern pattern1 = Pattern.compile("kazn(u|e)\\s*zatvora\\s*(u\\s*trajanju)?\\s*od\\s*(po)?\\s*(\\d+)\\s*(?:\\(\\s*[a-zčćđšžA-ZŽĐŠČĆ\\s*]+\\s*\\))?\\s*(?:\\/\\s*([a-zčćđšžA-ZŽĐŠČĆ\\s*]+)\\s*\\/)?\\s*([a-zčćđšžA-ZŽĐŠČĆ]+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        Pattern pattern2 = Pattern.compile("[kK]\\s*[rR]\\s*[iI]\\s*[vV]\\s*([iI]\\s*)?([jJ]\\s*[eE]|[Ss]\\s*[Uu])");
        Matcher matcher2 = pattern2.matcher(caseStr);
        int startIndex2 = 0;
        if(matcher2.find()){
            startIndex2 = matcher2.start();
        }

        while (matcher1.find()) {
            int startIndex1 = matcher1.start();
            if (startIndex1 > startIndex2) {
                String numberStr = matcher1.group(4);
                String periodStr = matcher1.group(6);
                return numberStr + " " + periodStr;
            }
        }

        return "unknown";

    }

    //radi za sve
    private String extractComunityServiceSentence(String caseStr) {
        Pattern pattern1 = Pattern.compile("kaznu\\s*rada\\s*u\\s*javnom\\s*interesu\\s*(u\\s*trajanju)?\\s*od\\s*(\\d+)\\s*(?:\\(\\s*[a-zčćđšžA-ZŽĐŠČĆ\\s*]+\\s*\\))?\\s*(?:\\/\\s*([a-zčćđšžA-ZŽĐŠČĆ\\s*]+)\\s*\\/)?\\s*([a-zčćđšžA-ZŽĐŠČĆ]+)");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String numberStr = matcher1.group(2);
            String periodStr = matcher1.group(4);
            return numberStr+" "+periodStr;
        }

        return "unknown";

    }


    //radi za sve, da li treba i deo u vezi clana??
    private String extractReasonForProsecution(String caseStr) {

        Pattern pattern1 = Pattern.compile("izvršio\\s*krivično\\s*djelo\\s*((šumska)?\\s*kra[djđ]+a|pusto[šs]enje\\s*šuma)\\s*iz\\s*čl(\\.)?\\s*(\\d+)\\s*(\\.)?\\s*st\\s*(\\.)?\\s*(\\d+)\\s*(\\.)?\\s*(u\\s*vezi\\s*st\\s*(\\.)?\\s*(\\d+))?");
        Matcher matcher1 = pattern1.matcher(caseStr);

        if (matcher1.find()) {
            String clan = matcher1.group(4);
            String stav = matcher1.group(7);

            return clan+" "+stav;
        }

        Pattern pattern2 = Pattern.compile("izvrši(o|li)\\s*krivično\\s*djelo\\s*((šumska)?\\s*kra[djđ]+a|pusto[šs]enje\\s*šuma)\\s*iz\\s*čl(ana)?\\s*(\\.)?\\s*(\\d+)\\s*(\\.)?\\s*st(av|ava)?\\s*(\\.)?\\s*(\\d+)\\s*(\\.)?\\s*(,)?\\s*(u\\s*vezi\\s*st\\s*(\\.)?\\s*(\\d+))?");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            String clan = matcher2.group(6);
            String stav = matcher2.group(10);

            return clan+" "+stav;
        }

        return "unknown";
    }

    // za 90/12 ne izvuce, mozda brka nesto
    private String extractCitedArticles(String caseStr) {
        Pattern pattern = Pattern.compile("\\s[Čč]l([.,]|(an))?\\s*[0-9]{1,3}(\\s*st[.,]\\s*[0-9]{1,3})?(\\s+u\\s+vezi\\s((st[.,]\\s*)|(stava\\s+))[0-9]{1,3})?(((,\\s*)|(\\s+i\\s+))([Čč]l([.,]|(an))?)?\\s*[0-9]{1,3}(\\s*st[.,]\\s*[0-9]{1,3})?)*\\s+((Krivičnog\\s+zakonika(\\s+Crne\\s+Gore)?)|(KZ\\s*CG)|(Zakonika\\s+o\\s+krivičnom\\s+postupku)|(ZKP-a)|(Zakona\\s+o\\s+duvanu))");
        Matcher matcher = pattern.matcher(caseStr);
        String ret = "";
        while (matcher.find()) {
            ret = ret + ", " + matcher.group();
        }
        return ret.replace("\r\n", " ").replace("\n", " ").trim();
    }

    //radi za sve
    private Boolean extractProtectedSurface(String caseStr) {
        Pattern pattern = Pattern.compile("u\\s*nacionalnom\\s*parku");
        Matcher matcher = pattern.matcher(caseStr);

        if (matcher.find()) {
            return Boolean.TRUE;
        }

        Pattern pattern2 = Pattern.compile("u\\s*zaštitnoj\\s*šumi");
        Matcher matcher2 = pattern2.matcher(caseStr);

        if (matcher2.find()) {
            return Boolean.TRUE;
        }

        Pattern pattern3 = Pattern.compile("u\\s*šumi\\s*sa\\s*posebnom\\s*namjenom");
        Matcher matcher3 = pattern3.matcher(caseStr);

        if (matcher3.find()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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
