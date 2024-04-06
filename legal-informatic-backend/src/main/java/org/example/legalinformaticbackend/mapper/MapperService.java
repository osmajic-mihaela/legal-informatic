package org.example.legalinformaticbackend.mapper;

import org.example.legalinformaticbackend.dto.DrDeviceLegalCaseDTO;
import org.example.legalinformaticbackend.model.DrDeviceLegalCase;
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
        var legalCase = new LegalCase();
        legalCase.setCaseNumber(legalCaseDescription.getCaseNumber() != null ? legalCaseDescription.getCaseNumber() : "");
        legalCase.setCourt(legalCaseDescription.getCourt() != null ? legalCaseDescription.getCourt() : "");
        legalCase.setJudge(legalCaseDescription.getJudge() != null ? legalCaseDescription.getJudge() : "");
        legalCase.setCourtReporter(legalCaseDescription.getCourtReporter() != null ? legalCaseDescription.getCourtReporter() : "");
        legalCase.setDefendant(legalCaseDescription.getDefendant() != null ? legalCaseDescription.getDefendant() : "");
        legalCase.setProtectedSurface(legalCaseDescription.getProtectedSurface() != null ? legalCaseDescription.getProtectedSurface().equals("true") : false);
        legalCase.setForestProperty(legalCaseDescription.getForestProperty() != null ? legalCaseDescription.getForestProperty() : "");
        legalCase.setFinancialDamage(legalCaseDescription.getFinancialDamage() != null ? Double.parseDouble(legalCaseDescription.getFinancialDamage()) : 0.0);
        legalCase.setWoodVolume(legalCaseDescription.getWoodVolume() != null ? Double.parseDouble(legalCaseDescription.getWoodVolume()) : 0.0);
        legalCase.setAwareness(legalCaseDescription.getAwareness() != null ? legalCaseDescription.getAwareness().equals("true") : false);
        legalCase.setConvicted(legalCaseDescription.getConvicted() != null ? legalCaseDescription.getConvicted().equals("true") : false);
        legalCase.setNumberOfTrees(legalCaseDescription.getNumberOfTrees() != null ? Integer.parseInt(legalCaseDescription.getNumberOfTrees()) : 0);
        legalCase.setTreeType(legalCaseDescription.getTreeType() != null ? legalCaseDescription.getTreeType() : "");
        legalCase.setReasonForProsecution(legalCaseDescription.getReasonForProsecution() != null ? legalCaseDescription.getReasonForProsecution() : "");
        legalCase.setConditionalSentence(legalCaseDescription.getConditionalSentence() != null ? legalCaseDescription.getConditionalSentence().equals("true") : false);
        legalCase.setPrisonSentence(legalCaseDescription.getPrisonSentence() != null ? legalCaseDescription.getPrisonSentence() : "");
        legalCase.setFinancialSentence(legalCaseDescription.getFinancialSentence()!= null ? Double.parseDouble(legalCaseDescription.getFinancialSentence()) : 0.0);
        legalCase.setCommunitySentence(legalCaseDescription.getCommunitySentence() != null ? legalCaseDescription.getCommunitySentence() : "");
        legalCase.setCitedArticles(legalCaseDescription.getCitedArticles()!= null ? legalCaseDescription.getCitedArticles() : "");
        return legalCase;
    }

    public DrDeviceLegalCase mapToDrDeviceLegalCase(DrDeviceLegalCaseDTO drDeviceLegalCaseDTO) {
        return modelMapper.map(drDeviceLegalCaseDTO, DrDeviceLegalCase.class);
    }
}
