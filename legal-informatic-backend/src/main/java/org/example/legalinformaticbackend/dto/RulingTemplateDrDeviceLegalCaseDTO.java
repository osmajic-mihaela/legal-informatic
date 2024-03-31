package org.example.legalinformaticbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RulingTemplateDrDeviceLegalCaseDTO {
    private DrDeviceLegalCaseDTO drDeviceLegalCaseDTO;
    private String ruling;
}
