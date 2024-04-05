package org.example.legalinformaticbackend.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LegalCaseDTO {

    private Long id;

    private String caseNumber;

    private String court;

    private String judge;

    private String plaintiff;

    private String courtReporter;

    private String defendant;

    private String protectedSurface;

    private String forestProperty;

    private String financialDamage;

    private String woodVolume;

    private String awareness;

    private String convicted;

    private String numberOfTrees;

    private String treeType;

    private String reasonForProsecution;

    private String conditionalSentence;

    private String prisonSentence;

    private String financialSentence;

    private String communitySentence;

    private String citedArticles;
}
