package main.project.tasks;

import main.project.HeuristicComparator;
import main.project.Individual;
import main.project.KungComparator;
import main.project.NaiveComparator;
import main.project.comparators.CrowdingValueSort;
import main.project.comparators.F1CrowdingSort;
import main.project.comparators.F2CrowdingSort;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nogaz on 28.05.2017.
 */
public abstract class MopImpl implements Mop {
    protected String filename;
    protected int terminationCondition = 10000;

    private final int initialPopulationSize = 8;
    private List<Individual> initialPopulation;

    private double sigma = 0.8d;
    private final double epsilon = 0.002d;
    private double tau = 0.01d;

    @Override
    public void doSimulation() {
        initialPopulation = generatePopulation();
        initialPopulation.stream().forEach(individual -> evaluate(individual));

        HeuristicComparator kung = new KungComparator();

        List<List<Individual>> frontsPopulation = generateDominanceDepthLayersByKung(initialPopulation);
        initialPopulation = new ArrayList<>();
        frontsPopulation.stream().forEach(individuals -> initialPopulation.addAll(individuals));

        List<Individual> childrenPopulation = new ArrayList<>();
        List<Individual> parentsPopulation = initialPopulation;
        List<Individual> combinedPopulation = new ArrayList<>();
        //List<List<Individual>> frontsPopulation = null;//generateDominanceDepthLayersByKung(initialPopulation);

        List<Individual> selectedPopulation = selection(parentsPopulation);
        List<Individual> mutatedPopulation = mutatePopulation(selectedPopulation);

        childrenPopulation.addAll(selectedPopulation);
        childrenPopulation.addAll(mutatedPopulation);

        while (terminationCondition > 0) {
            // 6)
            combinedPopulation = new ArrayList<>();
            combinedPopulation.addAll(parentsPopulation);
            combinedPopulation.addAll(childrenPopulation);

            // 7)
            parentsPopulation = new ArrayList<>();
            int i = 0;

            // 8)
            frontsPopulation = generateDominanceDepthLayersByKung(combinedPopulation);
            initialPopulation = new ArrayList<>();
            frontsPopulation.stream().forEach(individuals -> initialPopulation.addAll(individuals));

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
            for( int j = 0 ; j < remains ; ++j ){
                parentsPopulation.add(frontsPopulation.get(i).get(j));
            }
            // 13)
            //crowdingSort()
            selectedPopulation = selection(parentsPopulation);
            mutatedPopulation = mutatePopulation(selectedPopulation);

            childrenPopulation = new ArrayList<>();
            childrenPopulation.addAll(selectedPopulation);
            childrenPopulation.addAll(mutatedPopulation);

            terminationCondition--;
        }
        System.out.println(parentsPopulation.size());
        //final population
        saveToFile(parentsPopulation, this.filename);
    }


    public static void saveToFile(List<Individual> individuals, String filename){
        PrintWriter bw;
        FileWriter fw;
        try {
            fw = new FileWriter(filename);
            bw = new PrintWriter(fw);
            for (Individual individual: individuals) {
                //System.out.println(line);
                if( individual.getCoordinates().length == 3 ) {
                    bw.println(individual.getCoordinate(0) + " \t " + individual.getCoordinate(1) + " \t " + individual.getCoordinate(2) + " \t " + individual.getEvaluatedValue(0) + " \t " + individual.getEvaluatedValue(1));
                }else if( individual.getCoordinates().length == 1){
                    bw.println(individual.getCoordinate(0) + " \t " + individual.getEvaluatedValue(0) + " \t " + individual.getEvaluatedValue(1));
                }
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<Individual> mutatePopulation(final List<Individual> population){
        final List<Individual> mutatedPopulation = new ArrayList<>();
        for ( int i = 0 ; i < population.size() ; ++i ) {
            mutatedPopulation.add(mutateIndividual(population.get(i)));
        }
        return mutatedPopulation;
    }

    protected Individual mutateIndividual(final Individual individual/*, final double sigma*/){
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
    public List<Individual> generatePopulation() {
        List<Individual> population = new ArrayList<>();
        for( int i = 0 ; i < initialPopulationSize ; ++i ){
            population.add(generateIndividual());
        }
        return population;
    }

    public List<Individual> selection(final List<Individual> passedIndividuals) {

        List<Individual> selectedIndividuals = new ArrayList<>();
        List<Individual> individuals = setCrowdList(passedIndividuals);

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

    public List<Individual> setCrowdList(List<Individual> passedIndividuals){
        List<Individual> individuals = new ArrayList<>(passedIndividuals);
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
        return individuals;
    }

    public static List<List<Individual>> generateDominanceDepthLayersByKung(List<Individual> initialPopulation){
        List<Individual> initialIndividualsKung = new ArrayList<>(initialPopulation);
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

    public static List<Individual> readCsv(String title) throws IOException {
        List<Individual> individuals = new ArrayList<>();
        List<Double> numbers = new ArrayList<>();
        String readData;
        String line;
        BufferedReader br = null;
        String spliter = ";";
        try {
            br = new BufferedReader(new FileReader(title));
            while ((line = br.readLine()) != null) {
                String[] x = line.split(spliter);
                numbers.addAll(Arrays.stream(x).map(Double::new).collect(Collectors.toList()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        for (Double numb : numbers) {
            System.out.println(numb);
        }
        for (int i = 0; i < numbers.size(); i += 2) {
            individuals.add(new Individual(2, numbers.get(i), numbers.get(i + 1)));
        }
        return individuals;
    }


    @Override
    public List<Individual> crowdingSort(List<Individual> passedIndividuals) {
        List<Individual> individuals = new ArrayList<>(passedIndividuals);
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
        return individuals;
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
