package org.example.legalinformaticbackend.controller;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/law")
public class LawController {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    ResourcePatternResolver resourceResolver;

    @GetMapping("/laws-pdf")
    public ResponseEntity<?> getLawsPdf() throws IOException {
        List<String> retVal = new ArrayList<>();
        Resource[] resources = resourceResolver.getResources("classpath:law/*.pdf");
        for (Resource res: resources) {
            retVal.add(res.getFilename());
        }
        return ResponseEntity.ok()
                .body(retVal);
    }

    @GetMapping("/laws-pdf/{lawName}")
    public ResponseEntity<?> getLawPdf(@PathVariable String lawName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:law/" + lawName);
        Path path = Paths.get(resource.getURI());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(Files.readAllBytes(path));
    }

/*
    @PostMapping("/recommend-law-solution")
    public ResponseEntity<?> recommendCaseSolution(@RequestBody CaseDTO caseDTO) {
        return null
    }*/

    @GetMapping("/krivicni")
    public ResponseEntity<?> getLawsAkomaNtoso() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:akoma-ntoso/laws/krivicni.html");
        Path path = Paths.get(resource.getURI());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XHTML_XML)
                .body(Files.readAllBytes(path));
    }
}
