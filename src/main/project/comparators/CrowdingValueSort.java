package main.project.comparators;

import main.project.Individual;

import java.util.Comparator;

/**
 * Created by Nogaz on 30.05.2017.
 */
public class CrowdingValueSort implements Comparator<Individual> {
    //sort decr
    @Override
    public int compare(Individual o1, Individual o2) {
        double dif = o2.getCrowdingSortValue() - o2.getCrowdingSortValue();
        return (int) (dif * 10);
    }

}
