package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.repository.LegalCaseRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LegalCaseService {
    private final LegalCaseRepository legalCaseRepository;

    public LegalCase addNewCase(LegalCase legalCase) {
        return legalCaseRepository.save(legalCase);
    }
}
