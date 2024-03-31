package org.example.legalinformaticbackend.repository;
import es.ucm.fdi.gaia.jcolibri.casebase.LinealCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.legalinformaticbackend.model.LegalCase;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public interface LegalCaseRepository extends DbEntityRepository<LegalCase> {



}