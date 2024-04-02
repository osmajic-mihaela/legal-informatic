package org.example.legalinformaticbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RulingTemplateDrDeviceLegalCaseDTO {
    private DrDeviceLegalCaseDTO drDeviceLegalCaseDTO;
    // Response koji sam dobio od endpoint-a za rasudjivanje po pravilima
    private String ruling;
}
