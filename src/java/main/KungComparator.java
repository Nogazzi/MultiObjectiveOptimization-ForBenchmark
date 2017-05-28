package java.main;

import java.main.HeuristicComparator;
import java.main.Individual;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nogaz on 15.05.2017.
 */
public class KungComparator implements HeuristicComparator {
    @Override
    public List<Individual> identifyDominatedSet(final List<Individual> input) {
        if (input.size() == 1) {
            return input;
        }
        List<Individual> sortedInput = new ArrayList<>(input);
        Collections.sort(sortedInput);

        return front(sortedInput).stream().distinct().collect(Collectors.toList());
    }

    public List<Individual> front(List<Individual> p) {
        List<Individual> result = new ArrayList<>();
        if (p.size() == 1) {
            return p;
        } else {
            List<Individual> topPopulation = new ArrayList<>();
            List<Individual> bottomPopulation = new ArrayList<>();

            for (int i = 0; i < p.size(); ++i) {
                if (i < Math.floor(p.size() / 2)) {
                    topPopulation.add(p.get(i));
                } else {
                    bottomPopulation.add(p.get(i));
                }
            }
            topPopulation = front(topPopulation);
            bottomPopulation = front(bottomPopulation);

            result.addAll(topPopulation);

            for (int i = 0; i < bottomPopulation.size(); ++i) {
                if (!bottomPopulation.get(i).dominatedByAnyFrom(topPopulation)) {
                    result.add(bottomPopulation.get(i));
                }
            }
        }
        return result;
    }

}
