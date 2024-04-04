package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.model.DrDeviceLegalCase;
import org.example.legalinformaticbackend.model.LegalCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RulingTemplateService {
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
                "        p{\n" +
                "            margin-top: 30px;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "    </style>" +
                "</head>" + "\n<body>";
    }

    private String makeHTMLFooter() {
        return "</body>\n</html>";
    }

    private String makeLegalCaseBody(LegalCase legalCase) {
        return "\n<div id='case-body'>" +
                "\n<h3>U IME CRNE GORE</h3>" +
                "\n<div>" +
                "\n<p>" + legalCase.getCourt() + ", " +
                "sudija " + legalCase.getJudge() + ", " +
                "uz učešće zapisničara " + legalCase.getCourtReporter() + ", " +
                "u krivičnom predmetu " + legalCase.getCaseNumber() + ", protiv" +
                "okrivljenog " + legalCase.getDefendant() + ", u prisustvu državnog tužioca Odt - a" +
                legalCase.getPlaintiff() + " i okrivljenog " + legalCase.getDefendant() +
                " doneo je: </p>" +
                "</div>";
    }

    private String makeDrDeviceLegalCaseBody(DrDeviceLegalCase drDeviceLegalCase) {
        return "\n<div id='case-body'>" +
                "\n<h3>U IME CRNE GORE</h3>" +
                "\n<div>" +
                "\n<p>" + drDeviceLegalCase.getCourt() + ", " +
                "sudija " + drDeviceLegalCase.getJudge() + ", " +
                "uz učešće zapisničara " + drDeviceLegalCase.getCourtReporter() + ", " +
                "u krivičnom predmetu " + drDeviceLegalCase.getCaseNumber() + ", protiv" +
                "okrivljenog " + drDeviceLegalCase.getDefendant() + ", u prisustvu državnog tužioca Odt - a" +
                drDeviceLegalCase.getPlaintiff() + " i okrivljenog " + drDeviceLegalCase.getDefendant() +
                "doneo je: </p>" +
                "</div>";
    }

    private String makeRulingBody(String ruling) {
        return "\n<div id='ruling'>" +
                "\n<h3>PRESUDU</h3>" +
                "\n<div>\n<p>\n" + ruling + "\n</p>\n</div>" +
                "\n</div>" +
                "\n</div>";
    }

    public String makeLegalCaseTemplate(LegalCase legalCase, String ruling) {
        return this.makeHTMLHeader() +
                this.makeLegalCaseBody(legalCase) +
                this.makeRulingBody(ruling) +
                this.makeHTMLFooter();
    }

    public String makeDrDeviceLegalCaseTemplate(DrDeviceLegalCase drDeviceLegalCase, String ruling) {
        return this.makeHTMLHeader() +
                this.makeDrDeviceLegalCaseBody(drDeviceLegalCase) +
                this.makeRulingBody(ruling) +
                this.makeHTMLFooter();
    }
}
