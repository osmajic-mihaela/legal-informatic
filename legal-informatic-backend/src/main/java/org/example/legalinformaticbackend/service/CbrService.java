package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.connector.DatabaseConnector;
import org.example.legalinformaticbackend.model.LegalCase;
import org.example.legalinformaticbackend.model.TabularSimilarity;
import org.example.legalinformaticbackend.repository.LegalCaseRepository;
import org.springframework.stereotype.Service;
import es.ucm.fdi.gaia.jcolibri.casebase.LinealCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CbrService {
    Connector _connector;  /** Connector object */
    CBRCaseBase _caseBase;  /** CaseBase object */

    NNConfig simConfig;  /** KNN configuration */
    private final LegalCaseRepository legalCaseRepository;

    public void configure() throws ExecutionException {
        _connector =  new DatabaseConnector(legalCaseRepository);
        _caseBase = new LinealCaseBase();
        simConfig = configureKNN();
    }

    public List<String> recommend(CBRQuery query){
        try {
            configure();
            preCycle();
            List<String> retVal = getCycle(query);
            postCycle();
            return  retVal;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


    }

    private void cycle(CBRQuery query) throws ExecutionException {
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        eval = SelectCases.selectTopKRR(eval, 5);
        System.out.println("Retrieved cases:");
        for (RetrievalResult nse : eval)
            System.out.println(nse.get_case().getDescription() + " -> " + nse.getEval());
    }

    private List<String> getCycle(CBRQuery query) throws ExecutionException {
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        eval = SelectCases.selectTopKRR(eval, 5);
        List<String> retVal = new ArrayList<>();
        for (RetrievalResult nse : eval)
            retVal.add(nse.get_case().getDescription() + "<br /> Sličnost: " + String.format("%.2f", nse.getEval())+"<br />-------------------------------------------------------------");
        return  retVal;
    }

    private void postCycle() throws ExecutionException {

    }

    private CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        return _caseBase;
    }


    private NNConfig configureKNN(){

        simConfig = new NNConfig(); // KNN configuration

        simConfig.setDescriptionSimFunction(new Average());  // global similarity function = average


        //Similarity
        TabularSimilarity awarenessSimilarity = new TabularSimilarity(Arrays.asList(
                Boolean.TRUE,Boolean.FALSE));
        awarenessSimilarity.setSimilarity(Boolean.TRUE,Boolean.FALSE, 0.1);
        simConfig.addMapping(new Attribute("awareness", LegalCase.class), awarenessSimilarity);



        TabularSimilarity forestPropertySimilarity = new TabularSimilarity(Arrays.asList(
                "Državna",
                "Privatna"));
        forestPropertySimilarity.setSimilarity("Državna", "Privatna", 0.1);
        simConfig.addMapping(new Attribute("forestProperty", LegalCase.class), forestPropertySimilarity);



        TabularSimilarity isProtectedSurfaceSimilarity = new TabularSimilarity(Arrays.asList(
                Boolean.TRUE,Boolean.FALSE));
        isProtectedSurfaceSimilarity.setSimilarity(Boolean.TRUE,Boolean.FALSE,0.1);
        simConfig.addMapping(new Attribute("protectedSurface", LegalCase.class), isProtectedSurfaceSimilarity);



        TabularSimilarity treeTypeSimilarity = new TabularSimilarity(Arrays.asList(
                "hrastovih",
                "hrastova",
                "bukve",
                "bukovih",
                "bukovog",
                "graba",
                "grabovog",
                "smrčevo",
                "smrčeva",
                "smrčevih",
                "čamovih",
                "topola"));

        for (Object value1 : treeTypeSimilarity.getValues()) {
            for (Object value2 : treeTypeSimilarity.getValues()) {
                if (!value1.equals(value2) ) {
                    treeTypeSimilarity.setSimilarity(value1, value2, 0.1);
                }
            }
        }

        treeTypeSimilarity.setSimilarity("hrastovih", "hrastova", 1.0);
        treeTypeSimilarity.setSimilarity("bukve", "bukovih", 1.0);
        treeTypeSimilarity.setSimilarity("bukve", "bukovog", 1.0);
        treeTypeSimilarity.setSimilarity("bukovih", "bukovog", 1.0);
        treeTypeSimilarity.setSimilarity("graba", "grabovog", 1.0);
        treeTypeSimilarity.setSimilarity("smrčevo", "smrčeva", 1.0);
        treeTypeSimilarity.setSimilarity("smrčevo", "smrčevih", 1.0);
        treeTypeSimilarity.setSimilarity("smrčeva", "smrčevih", 1.0);
        simConfig.addMapping(new Attribute("treeType", LegalCase.class), treeTypeSimilarity);




        simConfig.addMapping(new Attribute("woodVolume", LegalCase.class), new Interval(10));
        simConfig.addMapping(new Attribute("numberOfTrees", LegalCase.class), new Interval(100));
        simConfig.addMapping(new Attribute("financialDamage", LegalCase.class), new Interval(1000));

        return simConfig;
        // Equal - returns 1 if both individuals are equal, otherwise returns 0
        // Interval - returns the similarity of two number inside an interval: sim(x,y) = 1-(|x-y|/interval)
        // Threshold - returns 1 if the difference between two numbers is less than a threshold, 0 in the other case
        // EqualsStringIgnoreCase - returns 1 if both String are the same despite case letters, 0 in the other case
        // MaxString - returns a similarity value depending of the biggest substring that belong to both strings
        // EnumDistance - returns the similarity of two enum values as the their distance: sim(x,y) = |ord(x) - ord(y)|
        // EnumCyclicDistance - computes the similarity between two enum values as their cyclic distance
        // Table - uses a table to obtain the similarity between two values. Allowed values are Strings or Enums. The table is read from a text file.
        // TabularSimilarity - calculates similarity between two strings or two lists of strings on the basis of tabular similarities

    }
}
