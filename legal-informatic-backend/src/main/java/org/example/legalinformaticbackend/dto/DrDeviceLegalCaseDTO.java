package org.example.legalinformaticbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DrDeviceLegalCaseDTO {

    private Long id;

    private String caseNumber;

    private String court;

    private String judge;

    private String courtReporter;

    private String defendant;

    private Boolean deforestation;

    private Boolean desolation;

    private Boolean prohibited_land;

    private Boolean special_forest;

    private Double woodVolume;

    private Boolean intention_to_sell;

    private Boolean had_intention;
}
