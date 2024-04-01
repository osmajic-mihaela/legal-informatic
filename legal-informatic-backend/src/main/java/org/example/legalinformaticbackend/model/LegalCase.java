package org.example.legalinformaticbackend.model;


import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import lombok.*;

import jakarta.persistence.*;

@Entity(name="LegalCase")
@Table(name = "legal_case")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class LegalCase extends DbEntity implements CaseComponent {
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

    @Override
    public Attribute getIdAttribute() {
        return null;
    }

    @Override
    public String toString() {
        String osudjen = "";
        String zasticenaPovrsina = "";
        String svestan = "";
        String uslovna = "";

        if(convicted){
            osudjen = "Osuđujuća";
        }else{
            osudjen = "Oslobađajuća";
        }

        if(protectedSurface){
            zasticenaPovrsina = "Da";
        }else{
            zasticenaPovrsina = "Ne";
        }

        if(awareness){
            svestan = "Svestan dela";
        }else{
            svestan = "Nesvestan dela";
        }

        if(conditionalSentence){
            uslovna = "Uslovna";
        }else{
            uslovna = "Neuslovna";
        }

        return "<br>" +
                "Broj slučaja: " + caseNumber  + "<br>" +
                "Sud: " + court  + "<br>" +
                "Sudija: " + judge  + "<br>" +
                "Zapisničar: " + courtReporter  + "<br>" +
                "Optuženi: " + defendant  + "<br>" +
                "Broj oborenih stabala: " + numberOfTrees + "<br>" +
                "Ukupna kubikaža: " + woodVolume + "m³<br>" +
                "Vrsta drveta: " + treeType +  "<br>" +
                "Svesnost izvršioca: " + svestan + "<br>" +
                "Pripadnost površine: " + forestProperty  + "<br>" +
                "Zaštićena površina: " + zasticenaPovrsina + "<br>" +
                "Pričinjena novčana šteta: " + financialDamage + "€<br>" +
                "Prekršaj: " + reasonForProsecution  + "<br>" +
                "Osuđen: " + osudjen + "<br>" +
                "Primenjeni propisi: " + citedArticles  + "<br>" +
                "Novčana kazna: " + financialSentence + "€<br>" +
                "Uslovnost kazne: " + uslovna + "<br>" +
                "Zatvorska kazna: " + prisonSentence  + "<br>" +
                "Kazna rada u javnom interesu: " + communitySentence  + "<br>";
        }
    }
