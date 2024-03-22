package org.example.legalinformaticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.model.DbEntity;
import org.example.legalinformaticbackend.service.AttributeExtractionService;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/case")
public class LegalCaseController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    ResourcePatternResolver resourceResolver;

    @Autowired
    AttributeExtractionService attributeExtractionService;


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

    @GetMapping("/extract-cases-attributes-from-pdf1")
    public ResponseEntity<?> extractCasesAttributesFromPdf1() throws IOException {
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

    @GetMapping("/extract-case-attributes-from-pdf1/{caseNumber}")
    public ResponseEntity<?> extractCaseAttributesFromPdf1(@PathVariable String caseNumber) throws IOException {
        DbEntity retVal = attributeExtractionService.attributeExtraction(caseNumber);
        return ResponseEntity.ok(retVal);
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

    @GetMapping("/extract-case-attributes-from-pdf/{caseNumber}")
    public ResponseEntity<?> extractCaseAttributesFromPdf(@PathVariable String caseNumber) throws IOException {
        DbEntity retVal = attributeExtractionService.attributeExtraction(caseNumber);
        return ResponseEntity.ok(retVal);
    }


    //Novi slucaj, csv, baza?
    //Preporuka odluke, funkcija slicnosti
    //Generisanje odluka po sablonu


    @GetMapping("/cases-akoma-ntoso")
    public ResponseEntity<?> getCasesAkomaNtoso() throws IOException {
        return null;
    }

    @GetMapping("/cases-akoma-ntoso/{caseName}")
    public ResponseEntity<?> getCaseAkomaNtoso(@PathVariable String caseName) throws IOException {
        return null;
    }

}
