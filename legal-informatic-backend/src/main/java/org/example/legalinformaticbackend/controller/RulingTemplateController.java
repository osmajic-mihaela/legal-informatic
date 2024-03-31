package org.example.legalinformaticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.dto.RulingTemplateDrDeviceLegalCaseDTO;
import org.example.legalinformaticbackend.dto.RulingTemplateLegalCaseDTO;
import org.example.legalinformaticbackend.mapper.MapperService;
import org.example.legalinformaticbackend.model.DrDeviceLegalCase;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.service.RulingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ruling-template")
public class RulingTemplateController {
    @Autowired
    RulingTemplateService rulingTemplateService;

    @Autowired
    MapperService mapperService;

    @PostMapping("/template-legal-case")
    public ResponseEntity<?> templateLegalCase(@RequestBody RulingTemplateLegalCaseDTO rulingTemplateLegalCaseDTO) {
        LegalCase legalCase = mapperService.mapToLegalCase(rulingTemplateLegalCaseDTO.getLegalCaseDTO());
        String ruling = rulingTemplateLegalCaseDTO.getRuling();

        String res = rulingTemplateService.makeLegalCaseTemplate(legalCase, ruling);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/template-dr-device-legal-case")
    public ResponseEntity<?> templateDrDeviceLegalCase(@RequestBody RulingTemplateDrDeviceLegalCaseDTO rulingTemplateDrDeviceLegalCaseDTO) {
        DrDeviceLegalCase drDeviceLegalCase = mapperService.mapToDrDeviceLegalCase(rulingTemplateDrDeviceLegalCaseDTO.getDrDeviceLegalCaseDTO());
        String ruling = rulingTemplateDrDeviceLegalCaseDTO.getRuling();

        String res = rulingTemplateService.makeDrDeviceLegalCaseTemplate(drDeviceLegalCase, ruling);

        return ResponseEntity.ok(res);
    }
}
