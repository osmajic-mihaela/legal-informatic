package org.example.legalinformaticbackend.controller;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.mapper.MapperService;
import org.example.legalinformaticbackend.model.DbEntity;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.dto.LegalCaseDTO;
import org.example.legalinformaticbackend.service.AttributeExtractionService;
import org.example.legalinformaticbackend.service.CbrService;
import org.example.legalinformaticbackend.service.LegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/case")
@CrossOrigin(origins = "http://localhost:4200")
public class LegalCaseController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    ResourcePatternResolver resourceResolver;

    @Autowired
    AttributeExtractionService attributeExtractionService;

    @Autowired
    MapperService mapperService;

    @Autowired
    CbrService cbrService;

    @Autowired
    LegalCaseService legalCaseService;


    @GetMapping("/cases-pdf")
    public ResponseEntity<?> getCasesPdf() throws IOException {
        List<String> retVal = new ArrayList<>();
        Resource[] resources = resourceResolver.getResources("classpath:cases/*.pdf");
        for (Resource res: resources) {
            retVal.add(res.getFilename());
        }
        return ResponseEntity.ok().body(retVal);
    }

    @GetMapping("/cases-pdf/{caseName}")
    public ResponseEntity<?> getCasePdf(@PathVariable String caseName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:cases/" + caseName);
        Path path = Paths.get(resource.getURI());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(Files.readAllBytes(path));
    }


    @GetMapping("/extract-cases-attributes-from-pdf")
    public ResponseEntity<?> extractCasesAttributesFromPdf() throws IOException {
        Resource[] resources = resourceResolver.getResources("classpath:cases/*.pdf");
        List<DbEntity> ret = new ArrayList<>();

        for(Resource resource : resources){
            String filePath = resource.getURI().toString();
            int lastSlashIndex = filePath.lastIndexOf('/');
            int lastDotIndex = filePath.lastIndexOf('.');
            String caseNmbr = filePath.substring(lastSlashIndex + 1, lastDotIndex);
            DbEntity retVal = attributeExtractionService.attributeExtraction(caseNmbr);
            ret.add(retVal);
        }


        return ResponseEntity.ok(ret);
    }
    @GetMapping("/preview-case-attributes/{caseName}")
    public ResponseEntity<?> previewCaseAttributes(@PathVariable String caseName) throws IOException {
        Resource resource = resourceResolver.getResource("classpath:cases/" + caseName + ".pdf");

        String filePath = resource.getURI().toString();
        int lastSlashIndex = filePath.lastIndexOf('/');
        int lastDotIndex = filePath.lastIndexOf('.');
        String caseNmbr = filePath.substring(lastSlashIndex + 1, lastDotIndex);
        return ResponseEntity.ok(attributeExtractionService.attributePreview(caseNmbr));
    }

    @GetMapping("/extract-case-attributes-from-pdf/{caseNumber}")
    public ResponseEntity<?> extractCaseAttributesFromPdf(@PathVariable String caseNumber) throws IOException {
        DbEntity retVal = attributeExtractionService.attributeExtraction(caseNumber);
        return ResponseEntity.ok(retVal);
    }

    @GetMapping("/cases-akoma-ntoso")
    public ResponseEntity<?> getCasesAkomaNtoso() throws IOException {
        List<String> retVal = new ArrayList<>();
        Resource[] resources = resourceResolver.getResources("classpath:akoma-ntoso/cases/*.html");
        for (Resource res: resources) {
            retVal.add(res.getFilename());
        }
        return ResponseEntity.ok()
                .body(retVal);
    }

    @GetMapping("/cases-akoma-ntoso/{caseName}")
    public ResponseEntity<?> getCaseAkomaNtoso(@PathVariable String caseName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:akoma-ntoso/cases/" + caseName);
        Path path = Paths.get(resource.getURI());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XHTML_XML)
                .body(Files.readAllBytes(path));
    }

    @PostMapping("/recommend-case-solution")
    public ResponseEntity<?> recommendCaseSolution(@RequestBody LegalCaseDTO caseDTO) {
        LegalCase legalCase = mapperService.mapToLegalCase(caseDTO);
        CBRQuery query = new CBRQuery();
        query.setDescription((CaseComponent) legalCase);
        List<String> retVal = cbrService.recommend(query);

        return ResponseEntity.ok(retVal);
    }

    @PostMapping("/add-new-case")
    public ResponseEntity<?> addNewCase(@RequestBody LegalCaseDTO caseDTO) {
        LegalCase legalCase = mapperService.mapToLegalCase(caseDTO);
        LegalCase retVal = legalCaseService.addNewCase(legalCase);

        return ResponseEntity.ok(retVal);
    }

}
