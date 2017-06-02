package test;

import main.project.Individual;
import main.project.comparators.F1CrowdingSort;
import main.project.comparators.F2CrowdingSort;
import main.project.tasks.Mop;
import main.project.tasks.MopOneImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nogaz on 02.06.2017.
 */
public class SortTest {

    @Test
    public void sortingTest(){
        List<Individual> passedIndividuals = new ArrayList<>();
        passedIndividuals.add(new Individual(10,6));
        passedIndividuals.add(new Individual(8,2));
        passedIndividuals.add(new Individual(6,10));
        passedIndividuals.add(new Individual(4,4));
        passedIndividuals.add(new Individual(2,8));
        Mop mop = new MopOneImpl();

        List<Individual> individuals = new ArrayList<>(passedIndividuals);
        individuals.stream().forEach(individual -> individual.setCrowdingSortValue(0));
        List<Individual> l1 = new ArrayList<>(individuals);
        List<Individual> l2 = new ArrayList<>(individuals);
        l1.sort(new F1CrowdingSort());
        l2.sort(new F2CrowdingSort());
        System.out.println(passedIndividuals);
    }
}
