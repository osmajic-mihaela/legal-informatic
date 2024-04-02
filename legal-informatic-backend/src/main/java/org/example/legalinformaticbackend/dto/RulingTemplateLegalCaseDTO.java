package org.example.legalinformaticbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RulingTemplateLegalCaseDTO {
    private LegalCaseDTO legalCaseDTO;

    // Response koji sam dobio od endpoint-a za rasudjivanje po slucajevima.
    private String ruling;
}
