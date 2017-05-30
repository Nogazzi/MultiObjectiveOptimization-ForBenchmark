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
        double dif = o2.getCoordinate(0) - o1.getCoordinate(0);
        if (dif == 0) {
            dif = o2.getCoordinate(1) - o1.getCoordinate(1);
        }
        return (int)(dif*10);
    }
}
