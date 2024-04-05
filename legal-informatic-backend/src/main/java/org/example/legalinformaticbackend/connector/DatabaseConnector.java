package org.example.legalinformaticbackend.connector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.*;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.repository.LegalCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DatabaseConnector implements Connector {

    @Autowired
    private final LegalCaseRepository legalCaseRepository;



    public DatabaseConnector(LegalCaseRepository legalCaseRepository) {
        this.legalCaseRepository = legalCaseRepository;
    }

    @Override
    public void initFromXMLfile(URL url) throws InitializingException {
    }

    @Override
    public void close() {

    }

    @Override
    public void storeCases(Collection<CBRCase> collection) {

    }

    @Override
    public void deleteCases(Collection<CBRCase> collection) {

    }

    @Override
    public Collection<CBRCase> retrieveAllCases() {
        List<LegalCase> legalCases = legalCaseRepository.findAll();
        LinkedList<CBRCase> cases = new LinkedList<CBRCase>();

        for (LegalCase legalCase : legalCases) {
            CBRCase cbrCase = new CBRCase();
            cbrCase.setDescription((CaseComponent) legalCase);
            cases.add(cbrCase);
        }

        return cases;
    }

    @Override
    public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter caseBaseFilter) {
        return null;
    }
}
