package test;

import main.project.Individual;
import main.project.comparators.F1CrowdingSort;
import main.project.comparators.F2CrowdingSort;
import main.project.tasks.Mop;
import main.project.tasks.MopImpl;
import main.project.tasks.MopOneImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nogaz on 30.05.2017.
 */
public class F1test {
    @Test
    public void testF2(){
        List<Individual> individuals = new ArrayList<>();
        Mop mop = new MopOneImpl();
        individuals = mop.generatePopulation();

        individuals.stream().forEach(individual -> System.out.println(individual.getEvaluatedValue(0)));

        System.out.println("POSPORTWOANE");
        individuals.sort(new F1CrowdingSort());
        individuals.stream().forEach(individual -> System.out.println(individual.getEvaluatedValue(0)));
    }

    @Test
    public void testF1(){
        List<Individual> individuals = new ArrayList<>();
        Mop mop = new MopOneImpl();
        individuals = mop.generatePopulation();


        individuals.stream().forEach(individual -> System.out.println(individual.getEvaluatedValue(1)));

        System.out.println("POSPORTWOANE");
        individuals.sort(new F2CrowdingSort());
        individuals.stream().forEach(individual -> System.out.println(individual.getEvaluatedValue(1)));
    }

    @Test
    public void testSort(){
        List<Individual> individuals = new ArrayList<>();
        Mop mop = new MopOneImpl();
        individuals = mop.generatePopulation();


        individuals.stream().forEach(individual -> System.out.println(individual.getEvaluatedValue(1)));

        System.out.println("POSPORTWOANE");
        Collections.sort(individuals);
        //individuals.sort(new F2CrowdingSort());
        individuals.stream().forEach(individual -> System.out.println(individual.getEvaluatedValue(0) + " \t " + individual.getEvaluatedValue(1)));
    }
}
