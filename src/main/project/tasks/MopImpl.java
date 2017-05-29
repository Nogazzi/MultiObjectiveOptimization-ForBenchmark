package main.project.tasks;

import main.project.HeuristicComparator;
import main.project.Individual;
import main.project.KungComparator;
import main.project.comparators.F1CrowdingSort;
import main.project.comparators.F2CrowdingSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nogaz on 28.05.2017.
 */
public abstract class MopImpl implements Mop {

    protected int terminationCondition = 100000000;

    private int initialPopulationSize = 30;
    private List<Individual> initialPopulation;

    @Override
    public void doSimulation() {
        initialPopulation = new ArrayList<>();
        generatePopulation();
        initialPopulation.stream().forEach(individual -> evaluate(individual));

        HeuristicComparator kung = new KungComparator();

        initialPopulation = generateDominanceDepthLayersByKung(initialPopulation);

        List<Individual> childrenPopulation;
        List<Individual> parentsPopulation;
        List<Individual> combinedPopulation;
        while (terminationCondition > 0) {
            // 6)
            combinedPopulation.addAll(childrenPopulation);
            combinedPopulation.addAll(parentsPopulation);
            // 7)
            parentsPopulation = new ArrayList<>();
            // 8)
            identifyFronts(combinedPopulation);

        }
    }

    @Override
    public void generatePopulation() {
        for( int i = 0 ; i < initialPopulationSize ; ++i ){
            initialPopulation.add(generateIndividual());
        }
    }

    public static List<Individual> generateDominanceDepthLayersByKung(List<Individual> initialIndividualsKung){
        int counter = 0;
        List<List<Individual>> dominanceRankKung = new ArrayList<>();
        while( initialIndividualsKung.size() > 0 ) {
            dominanceRankKung.add(new ArrayList<Individual>(simulateByKungAlgorithm(initialIndividualsKung)));
            for( int i = 0 ; i < dominanceRankKung.get(counter).size() ; ++i ){
                if( initialIndividualsKung.contains(dominanceRankKung.get(counter).get(i))){
                    dominanceRankKung.get(counter).get(i).setDominanceDepth(counter);
                    initialIndividualsKung.remove(dominanceRankKung.get(counter).get(i));
                }
            }
            counter++;
        }
        return dominanceRankKung.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public static List<Individual> simulateByKungAlgorithm(List<Individual> individuals){
        individuals = individuals.stream().distinct().collect(Collectors.toList());
        Collections.sort(individuals);
        HeuristicComparator kungComparator = new KungComparator();
        List<Individual> kungIdentifiedSet = kungComparator.identifyDominatedSet(individuals);
        kungIdentifiedSet = kungIdentifiedSet.stream().distinct().collect(Collectors.toList());
        return kungIdentifiedSet;
    }

    @Override
    public List<Individual> crowdingSort(List<Individual> individuals) {
        individuals.stream().forEach(individual -> individual.setCrowdDistance(0));
        List<Individual> l1 = new ArrayList<>(individuals);
        List<Individual> l2 = new ArrayList<>(individuals);

        l1.sort(new F1CrowdingSort());
        l2.sort(new F2CrowdingSort());

        for(int i = 0 ; i < l1.size() ; ++i) {

        }
    }

    @Override
    public void identifyFronts(List<Individual> individuals) {
        individuals = individuals.stream().distinct().collect(Collectors.toList());
        Collections.sort(individuals);
        HeuristicComparator kungComparator = new KungComparator();

        List<Individual> kungIdentifiedSet = kungComparator.identifyDominatedSet(individuals);
        kungIdentifiedSet = kungIdentifiedSet.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public void calculateCrowdDistance(List<Individual> individuals) {

    }
}
