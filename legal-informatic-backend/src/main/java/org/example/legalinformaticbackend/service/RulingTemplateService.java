package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.model.DrDeviceLegalCase;
import org.example.legalinformaticbackend.model.LegalCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RulingTemplateService {

    @Autowired
    private DrDeviceLegalCaseService drDeviceLegalCaseService;
    private String makeHTMLHeader() {
        return "<html><head>" +
                "<style>\n" +
                "        h1,\n" +
                "        h2,\n" +
                "        h3,\n" +
                "        h4 {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        h3 {\n" +
                "            margin-bottom: 60px;\n" +
                "        }\n" +
                "\n" +
                "        h4 {\n" +
                "            margin-top: 50px;\n" +
                "            margin-bottom: 50px;\n" +
                "        }\n" +
                "\n" +
                "    </style>" +
                "</head>" + "\n<body>";
    }

    private String makeHTMLTitle(String court, String caseNumber, String date) {
        return "\n\n<div id=\"title\" style=\"text-align: center;\">" +
                "<p>" + court + "</p>\n" +
                "<h1>" + caseNumber + "</h1>\n" +
                "<p>" + date + "</p>\n" +
                "</div>";
    }

    private String makeHTMLCaseNumber(String caseNumber) {
        return "\n\n<div id=\"kbr\" style=\"text-align: right; margin-right: 5%; margin-top: 50px; margin-bottom: 50px;\">\n" +
                "<p>" + caseNumber + "</p>\n" +
                "</div>";
    }

    private String makeHTMLPrelude(String court, String judge, String courtReporter, String defendant, String charges, String date) {
        return "\n\n<div id=\"prelude\" style=\"margin-right: 15%; margin-left: 15%; margin-top: 50px; margin-bottom: 50px\">\n" +
                "<h2>U IME CRNE GORE</h2>\n" +
                "<p style=\"text-align: justify;\">\n" +
                court + ", " + judge + ", uz učešće zapisničara " + courtReporter + ", okrivljenog " + defendant + ", krivog za " + charges +
                "dana " + date + " godine, doneo je i javno objavio" +
                "</p>\n" +
                "</div>";
    }

    private String makeHTMLJudgement(String defendant, String judgementMeta) {
        return "\n\n<div id=\"judgment\" style=\"margin-right: 15%; margin-left: 15%; margin-top: 50px; margin-bottom: 50px\">\n" +
                "<h2>PRESUDU</h2>\n" +
                "<p style=\"text-align: justify;\">\n" +
                "Okrivljeni " + defendant + ", " + judgementMeta + "\n" +
                "</p>\n" +
                "</div>";
    }

    private String makeHTMLIsGuilty(String date, String verdict) {
        return "\n\n<div id=\"is-guily\" style=\"margin-right: 15%; margin-left: 15%; margin-top: 50px; margin-bottom: 50px\">\n" +
                "<h2>KRIV JE</h2>\n" +
                "<p style=\"text-align: left;\">\n" +
                "Što je:\n" +
                "</p>\n" +
                "<p style=\"text-align: justify;\">\n" +
                "dana " + date + " godine, " + verdict + "\n" +
                "</p>\n" +
                "</div>";
    }

    private String makeHTMLPenalty(String penalties) {
        return "\n\n<div id=\"penalty\" style=\"margin-right: 15%; margin-left: 15%; margin-top: 50px; margin-bottom: 50px\">\n" +
                "<h2>OSUĐENJE</h2>\n" +
                "<p style=\"text-align: justify;\">\n" +
                penalties + "\n" +
                "</p>\n" +
                "</div>";
    }

    private String makeHTMLExplanation(String explanationMeta) {
        return "<div id=\"explanation\" style=\"margin-right: 15%; margin-left: 15%; margin-top: 50px; margin-bottom: 50px\">\n" +
                "<h2>Obrazloženje</h2>\n" +
                "<p style=\"text-align: justify;\">\n" +
                explanationMeta + "\n" +
                "</p>" +
                "</div>";
    }

    private String makeHTMLFooter() {
        return "\n\n</body>\n</html>";
    }

    private String makeLegalCaseBody(LegalCase legalCase, String date, String judgementMeta, String explanationMeta) {
        return this.makeHTMLTitle(legalCase.getCourt(), legalCase.getCaseNumber(), date) +
                this.makeHTMLCaseNumber(legalCase.getCaseNumber()) +
                this.makeHTMLPrelude(legalCase.getCourt(), legalCase.getJudge(), legalCase.getCourtReporter(), legalCase.getDefendant(), legalCase.getCitedArticles(), date) +
                this.makeHTMLJudgement(legalCase.getDefendant(), judgementMeta) +
                this.makeHTMLIsGuilty(date, legalCase.toStringVerdict()) +
                this.makeHTMLPenalty(legalCase.toStringPenalties()) +
                this.makeHTMLExplanation(explanationMeta) +
                this.makeHTMLFooter();
    }

    private String makeDrDeviceLegalCaseBody(DrDeviceLegalCase drDeviceLegalCase, String ruling, String date, String judgementMeta, String explanationMeta) {
        ruling = ruling.replace("\n", ", ").toLowerCase();

        return this.makeHTMLTitle(drDeviceLegalCase.getCourt(), drDeviceLegalCase.getCaseNumber(), date) +
                this.makeHTMLCaseNumber(drDeviceLegalCase.getCaseNumber()) +
                this.makeHTMLPrelude(drDeviceLegalCase.getCourt(), drDeviceLegalCase.getJudge(), drDeviceLegalCase.getCourtReporter(), drDeviceLegalCase.getDefendant(), this.drDeviceLegalCaseService.getVerdictArticles(ruling), date) +
                this.makeHTMLJudgement(drDeviceLegalCase.getDefendant(), judgementMeta) +
                this.makeHTMLIsGuilty(date, ruling) +
                this.makeHTMLPenalty(this.drDeviceLegalCaseService.getVerdictPenalties(ruling)) +
                this.makeHTMLExplanation(explanationMeta) +
                this.makeHTMLFooter();
    }

    public String makeLegalCaseTemplate(LegalCase legalCase, String date, String judgementMeta, String explanationMeta) {
        return this.makeHTMLHeader() +
                this.makeLegalCaseBody(legalCase, date, judgementMeta, explanationMeta) +
                this.makeHTMLFooter();
    }

    public String makeDrDeviceLegalCaseTemplate(DrDeviceLegalCase drDeviceLegalCase, String ruling, String date, String judgementMeta, String explanationMeta) {
        if (ruling.equals("Optuženi nije pronađen kriv ni po jednoj stavci")) {
            return "<html><body><p>Optuženi nije pronađen kriv ni po jednoj stavci</p></body></html>";
        }

        return this.makeHTMLHeader() +
                this.makeDrDeviceLegalCaseBody(drDeviceLegalCase, ruling, date, judgementMeta, explanationMeta) +
                this.makeHTMLFooter();
    }
}
