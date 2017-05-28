package java.main.comparators;


import java.main.Individual;
import java.util.Comparator;

/**
 * Created by Nogaz on 22.05.2017.
 */
public class DominanceRankComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return o1.getDominanceRank() - o2.getDominanceRank();
    }
}
