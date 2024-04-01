package org.example.legalinformaticbackend.model;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name="DrDeviceLegalCase")
@Table(name="legal_case")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DrDeviceLegalCase extends DbEntity implements CaseComponent {

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

    @Column(name = "deforestation")
    private Boolean deforestation;

    @Column(name = "desolation")
    private Boolean desolation;

    // i.e. parks, tree lines, etc.
    @Column(name = "prohibited_land")
    private Boolean prohibited_land;

    // i.e. national park
    @Column(name = "special_forest")
    private Boolean special_forest;

    @Column(name = "wood_volume")
    private Double woodVolume;

    @Column(name = "intention_to_sell")
    private Boolean intention_to_sell;

    @Column(name = "had_intention")
    private Boolean had_intention;

    @Override
    public Attribute getIdAttribute() {
        return null;
    }

    @Override
    public String toString() {
        return "DrDeviceLegalCase{" +
                " id=" + id +
                " case_number=" + caseNumber +
                " court=" + court +
                " judge=" + judge +
                " courtReporter=" + courtReporter +
                " defendant=" + defendant +
                " deforestation=" + deforestation +
                " desolation=" + desolation +
                " prohibited_land=" + prohibited_land +
                " special_forest=" + special_forest +
                " wood_volume=" + woodVolume +
                " intention_to_sell=" + intention_to_sell +
                " had_intention=" + had_intention + " }";
    }
}
