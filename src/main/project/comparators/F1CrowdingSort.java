package main.project.comparators;

import main.project.Individual;

import java.util.Comparator;

/**
 * Created by Nogaz on 29.05.2017.
 */
public class F1CrowdingSort implements Comparator<Individual>{
    //sort decr
    @Override
    public int compare(Individual o1, Individual o2) {
        int dif = Double.compare(o2.getEvaluatedValue(0), o1.getEvaluatedValue(0));
        if (dif == 0) {
            return Double.compare(o2.getEvaluatedValue(1), o1.getEvaluatedValue(1));

        }
        return dif;
    }
}
