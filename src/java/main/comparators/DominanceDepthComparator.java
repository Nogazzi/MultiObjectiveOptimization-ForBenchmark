package java.main.comparators;


import java.main.Individual;
import java.util.Comparator;

/**
 * Created by Nogaz on 22.05.2017.
 */
public class DominanceDepthComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return o2.getDominanceDepth()-o1.getDominanceDepth();
    }
}
