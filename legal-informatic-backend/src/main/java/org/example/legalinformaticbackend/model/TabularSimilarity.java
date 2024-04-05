package org.example.legalinformaticbackend.model;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jakarta.persistence.Entity;
import java.util.List;

import java.util.List;

public class TabularSimilarity implements LocalSimilarityFunction {
    private double matrix[][];
    List<Object> categories;

    public TabularSimilarity(List<Object> categories) {
        this.categories = categories;
        int n = categories.size();
        matrix = new double[n][n];
        for (int i=0; i<n; i++)
            matrix[i][i] = 1;
    }

    public void setSimilarity(Object value1, Object value2, double sim) {
        setSimilarity(value1, value2, sim, sim);
    }

    private void setSimilarity(Object value1, Object value2, double sim1, double sim2) {
        int index1 = categories.indexOf(value1);
        int index2 = categories.indexOf(value2);
        if (index1 != -1 && index2 != -1) {
            matrix[index1][index2] = sim1;
            matrix[index2][index1] = sim2;
        }
    }


    public double getSimilarity(Object value1, Object value2) {
        int index1 = categories.indexOf(value1);
        int index2 = categories.indexOf(value2);
        if (index1 != -1 && index2 != -1) {
            return matrix[index1][index2];
        }
        return 0.0;
    }

    public List<Object> getValues() {
        return categories;
    }


    @Override
    public double compute(Object value1, Object value2) throws NoApplicableSimilarityFunctionException {
        if (value1 instanceof String && value2 instanceof String)
            return compute((String) value1, (String) value2);
        if (value1 instanceof List && value2 instanceof List)
            return compute((List<?>) value1, (List<?>) value2);
        if (value1 instanceof Boolean && value2 instanceof Boolean)
            return compute((Boolean) value1, (Boolean) value2);
        //if (value1 instanceof Double && value2 instanceof Double)
        //    return compute((Double) value1, (Double) value2);
        //if (value1 instanceof Integer && value2 instanceof Integer)
        //    return compute((Integer) value1, (Integer) value2);
        return 0;
    }

    public double compute(Boolean bool1, Boolean bool2) {
        return getSimilarity(bool1, bool2);
    }

    public double compute(String str1, String str2) {
        return getSimilarity(str1, str2);
    }

    public double compute(List<String> list1, List<String> list2) {
        if (list1.isEmpty() && list2.isEmpty())
            return 1;
        double sim1to2 = 0;
        for (String el1: list1) {
            double sim = 0;
            for (String el2: list2)
                sim = Math.max(sim, compute(el1,el2));
            sim1to2 += sim;
        }
        double sim2to1 = 0;
        for (String el2: list2) {
            double sim = 0;
            for (String el1: list1)
                sim = Math.max(sim, compute(el2, el1));
            sim2to1 += sim;
        }
        return (sim1to2 + sim2to1)/(list1.size() + list2.size());
    }

    @Override
    public boolean isApplicable(Object value1, Object value2) {
        if (value1 instanceof String && value2 instanceof String)
            return true;
        if (value1 instanceof List && value2 instanceof List)
            return true;
        if (value1 instanceof Boolean && value2 instanceof Boolean)
            return true;
        return false;
    }




}
