package org.example.legalinformaticbackend.mapper;

import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.dto.LegalCaseDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class MapperService {

    private final ModelMapper modelMapper;

    public MapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    public LegalCaseDTO mapToLegalCaseDescription(LegalCase legalCase) {
        return modelMapper.map(legalCase, LegalCaseDTO.class);
    }

    public LegalCase mapToLegalCase(LegalCaseDTO legalCaseDescription) {
        return modelMapper.map(legalCaseDescription, LegalCase.class);
    }
}
