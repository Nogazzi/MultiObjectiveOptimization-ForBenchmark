package main.project;

import main.project.tasks.Mop;
import main.project.tasks.MopOneImpl;
import main.project.tasks.MopTwoImpl;
import org.junit.Assert;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nogaz on 28.05.2017.
 */
public class Main {
    public static void main(String[] args){

        Mop mop = new MopOneImpl();
        mop.doSimulation();

        Mop mopTwo = new MopTwoImpl();
        mopTwo.doSimulation();
    }

    public static void ble() throws IOException {
        Mop mop1Impl = new MopOneImpl();
        List<Individual> individuals = MopOneImpl.readCsv("C:\\Users\\Nogaz\\IdeaProjects\\Multi-ObjectiveOptimization\\Kung-danetestowe.csv");
        individuals.stream().forEach(individual -> mop1Impl.evaluate(individual));
        List<Individual> results = MopOneImpl.simulateByKungAlgorithm(individuals);
        HeuristicComparator kungComparator = new KungComparator();
        List<Individual> results2 = kungComparator.identifyDominatedSet(individuals);
        List<Individual> results3 = new ArrayList<>();
        //Assert.assertEquals(85, results.size());
        List<List<Individual>> lists = MopOneImpl.generateDominanceDepthLayersByKung(individuals);
        //MopOneImpl.saveToFile(lists.get(0), "test.txt");
        lists.stream().forEach(individuals1 -> results3.addAll(individuals1));
        Assert.assertEquals(85, results3.size());

        MopOneImpl.saveToFile(lists.get(0), "test.txt");
    }
}
