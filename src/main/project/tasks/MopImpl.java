package main.project.tasks;

import main.project.HeuristicComparator;
import main.project.Individual;
import main.project.KungComparator;
import main.project.comparators.CrowdingValueSort;
import main.project.comparators.F1CrowdingSort;
import main.project.comparators.F2CrowdingSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Nogaz on 28.05.2017.
 */
public abstract class MopImpl implements Mop {

    protected int terminationCondition = 100000000;

    private int initialPopulationSize = 30;
    private List<Individual> initialPopulation;

    private double sigma = 0.8d;
    private final double epsilon = 0.002d;
    private double tau = 0.01d;

    @Override
    public void doSimulation() {
        initialPopulation = new ArrayList<>();
        generatePopulation();
        initialPopulation.stream().forEach(individual -> evaluate(individual));

        HeuristicComparator kung = new KungComparator();

        List<Individual> childrenPopulation = new ArrayList<>();
        List<Individual> parentsPopulation = initialPopulation;
        List<Individual> combinedPopulation = new ArrayList<>();

        List<List<Individual>> frontsPopulation = generateDominanceDepthLayersByKung(initialPopulation);

        List<Individual> selectedPopulation = selection(parentsPopulation);
        List<Individual> mutatedPopulation = mutatePopulation(selectedPopulation);

        childrenPopulation.addAll(selectedPopulation);
        childrenPopulation.addAll(mutatedPopulation);

        while (terminationCondition > 0) {
            // 6)
            combinedPopulation.addAll(parentsPopulation);
            combinedPopulation.addAll(childrenPopulation);

            // 7)
            parentsPopulation = new ArrayList<>();
            int i = 0;

            // 8)
            frontsPopulation = generateDominanceDepthLayersByKung(combinedPopulation);

            // 9)
            while(parentsPopulation.size() + frontsPopulation.get(i).size() <= initialPopulationSize){
                // 10)
                crowdingSort(frontsPopulation.get(i));
                // 11)
                parentsPopulation.addAll(frontsPopulation.get(i));
                ++i;
            }
            // 13)
            crowdingSort(frontsPopulation.get(i));
            int remains = initialPopulationSize - parentsPopulation.size();

            // 13)
            //crowdingSort()
        }

        //final population
    }

    private List<Individual> mutatePopulation(final List<Individual> population){
        final List<Individual> mutatedPopulation = new ArrayList<>();
        for ( int i = 0 ; i < population.size() ; ++i ) {
            mutatedPopulation.add(mutateIndividual(population.get(i)));

        }
        return mutatedPopulation;
    }

    private Individual mutateIndividual(final Individual individual/*, final double sigma*/){
        Random generator = new Random();
        final Individual newIndividual;
        sigma = sigma * Math.exp(tau*generator.nextGaussian());
        if( sigma < epsilon ){
            sigma = epsilon;
        }
        double[] newCoordinates = new double[individual.getCoordinates().length];
        for(int i = 0 ; i < individual.getCoordinates().length ; ++i){
            newCoordinates[i] = individual.getCoordinate(i) + generator.nextGaussian()*sigma;
        }
        newIndividual = new Individual(individual.getSize(),newCoordinates);
        evaluate(newIndividual);
        return newIndividual;
    }

    @Override
    public void generatePopulation() {
        for( int i = 0 ; i < initialPopulationSize ; ++i ){
            initialPopulation.add(generateIndividual());
        }
    }

    public List<Individual> selection(final List<Individual> individuals) {

        List<Individual> selectedIndividuals = new ArrayList<>();
        setCrowdList(individuals);

        for( int i = 0 ; i < individuals.size() ; i+=2){
            if(individuals.get(i).getDominanceDepth() < individuals.get(i+1).getDominanceDepth()){
                selectedIndividuals.add(individuals.get(i));
            }else if(individuals.get(i).getDominanceDepth() > individuals.get(i+1).getDominanceDepth()){
                selectedIndividuals.add(individuals.get(i+1));
            }else{
                if( individuals.get(i).getCrowdingSortValue() > individuals.get(i+1).getCrowdingSortValue()){
                    selectedIndividuals.add(individuals.get(i));
                }else{
                    selectedIndividuals.add(individuals.get(i+1));
                }
            }
        }

        return selectedIndividuals;
    }

    public void setCrowdList(List<Individual> individuals){
        individuals.sort(new F1CrowdingSort());
        for( int i = 0 ; i < individuals.size() ; ++i){
            if( i==0){
                individuals.get(i).setCrowdingSortValue(Double.MAX_VALUE);
            }else if( i== individuals.size()-1){
                individuals.get(i).setCrowdingSortValue(Double.MAX_VALUE);
            }else{
                double crowdDist = Math.abs(individuals.get(i-1).getEvaluatedValue(0) - individuals.get(i+1).getEvaluatedValue(0)) +
                        Math.abs(individuals.get(i-1).getEvaluatedValue(1) - individuals.get(i+1).getEvaluatedValue(1));

                individuals.get(i).setCrowdingSortValue(crowdDist);
            }
        }
    }

    public static List<List<Individual>> generateDominanceDepthLayersByKung(List<Individual> initialIndividualsKung){
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
        return dominanceRankKung;
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
    public void/*List<Individual>*/ crowdingSort(List<Individual> individuals) {
        individuals.stream().forEach(individual -> individual.setCrowdingSortValue(0));
        List<Individual> l1 = new ArrayList<>(individuals);
        List<Individual> l2 = new ArrayList<>(individuals);


        l1.sort(new F1CrowdingSort());
        l2.sort(new F2CrowdingSort());

        List<List<Individual>> l = new ArrayList<>();
        l.add(l1);
        l.add(l2);

        l1.get(0).setCrowdingSortValue(Double.MAX_VALUE);
        l1.get(individuals.size()-1).setCrowdingSortValue(Double.MAX_VALUE);

        l2.get(0).setCrowdingSortValue(Double.MAX_VALUE);
        l2.get(individuals.size()-1).setCrowdingSortValue(Double.MAX_VALUE);
        for( int m = 0 ; m < l.size() ; ++m){
            l.get(m).get(0).setCrowdingSortValue(Double.MAX_VALUE);
            l.get(m).get(l.get(m).size()-1).setCrowdingSortValue(Double.MAX_VALUE);
            for(int j = 1 ; j < individuals.size()-1 ; ++j) {
                double d = l.get(m).get(j).getCrowdingSortValue();
                d += (l.get(m).get(j+1).getEvaluatedValue(m) - l.get(m).get(j-1).getEvaluatedValue(m)) /
                        l.get(m).get(0).getEvaluatedValue(m) - l.get(m).get(l.get(m).size()-1).getEvaluatedValue(m);
                l.get(m).get(j).setCrowdingSortValue(d);
            }
        }
        individuals.sort(new CrowdingValueSort());
    }

    /*@Override
    public void identifyFronts(List<Individual> individuals) {
        individuals = individuals.stream().distinct().collect(Collectors.toList());
        Collections.sort(individuals);
        HeuristicComparator kungComparator = new KungComparator();

        List<Individual> kungIdentifiedSet = kungComparator.identifyDominatedSet(individuals);
        kungIdentifiedSet = kungIdentifiedSet.stream().distinct().collect(Collectors.toList());
    }*/

    @Override
    public void calculateCrowdDistance(List<Individual> individuals) {

    }
}
