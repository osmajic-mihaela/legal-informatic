package org.example.legalinformaticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOCase;
import org.example.legalinformaticbackend.dto.DrDeviceLegalCaseDTO;
import org.example.legalinformaticbackend.dto.VerdictDTO;
import org.example.legalinformaticbackend.mapper.MapperService;
import org.example.legalinformaticbackend.model.DrDeviceLegalCase;
import org.example.legalinformaticbackend.service.DrDeviceLegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dr-device-cases")
public class DrDeviceLegalCaseController {
    @Autowired
    DrDeviceLegalCaseService legalCaseService;

    @Autowired
    MapperService mapperService;

    @PostMapping("/recommend-case-verdict")
    public ResponseEntity<?> recommendCaseVerdict(@RequestBody DrDeviceLegalCaseDTO legalCaseDTO) {
        DrDeviceLegalCase legalCase = mapperService.mapToDrDeviceLegalCase(legalCaseDTO);

        // add case
        legalCaseService.addDrDeviceLegalCase(legalCase);

        // make facts
        legalCaseService.writeFactsRDF(legalCase);

        // call start
        legalCaseService.runStartBat();

        // read export
        try {
            String verdict = legalCaseService.parseExportRDF();

            // call clean
            legalCaseService.runCleanBat();

            // make pdf?
            var verdictDto = new VerdictDTO();
            verdictDto.verdict = verdict;
            return ResponseEntity.ok(verdictDto);
        }
        catch (IOException e) {
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }
    }
}
