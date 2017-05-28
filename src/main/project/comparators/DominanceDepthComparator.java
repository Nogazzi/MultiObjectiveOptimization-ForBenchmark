package main.project.comparators;


import main.project.Individual;

import java.util.Comparator;

/**
 * Created by Nogaz on 22.05.2017.
 */
public class DominanceDepthComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return o1.getDominanceDepth()-o2.getDominanceDepth();
    }
}
