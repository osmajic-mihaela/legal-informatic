package org.example.legalinformaticbackend.model;


import lombok.*;

import jakarta.persistence.*;

@Entity(name="LegalCase")
@Table(name = "legal_case")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class LegalCase extends DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_number")
    private String caseNumber;

    @Column(name = "court")
    private String court;

    @Column(name = "judge")
    private String judge;

    @Column(name = "court_reporter")
    private String courtReporter;

    @Column(name = "defendant")
    private String defendant;

    @Column(name = "is_protected_surface")
    private Boolean protectedSurface;

    @Column(name = "forest_property")
    private String forestProperty;

    @Column(name = "financial_damage")
    private Double financialDamage;

    @Column(name = "wood_volume")
    private Double woodVolume;

    @Column(name = "is_awareness")
    private Boolean awareness;

    @Column(name = "is_convicted")
    private Boolean convicted;

    @Column(name = "number_of_trees")
    private Integer numberOfTrees;

    @Column(name = "tree_type")
    private String treeType;

    @Column(name = "reason_for_prosecution")
    private String reasonForProsecution;

    @Column(name = "is_conditional_sentence")
    private Boolean conditionalSentence;

    @Column(name = "prison_sentence")
    private String prisonSentence;

    @Column(name = "financial_sentence")
    private Double financialSentence;

    @Column(name = "community_sentence")
    private String communitySentence;

    @Column(name = "cited_articles", length = 1000)
    private String citedArticles;
}