package main.project.comparators;

import main.project.Individual;

import java.util.Comparator;

/**
 * Created by Nogaz on 29.05.2017.
 */
public class F2CrowdingSort implements Comparator<Individual> {
    //sort decr
    @Override
    public int compare(Individual o1, Individual o2) {
        double dif = o2.getEvaluatedValue(1) - o1.getEvaluatedValue(1);
        if (dif == 0) {
            dif = o2.getEvaluatedValue(0) - o1.getEvaluatedValue(0);
        }
        return (int)(dif*10);
    }
}
