package main.project.tasks;

import main.project.Individual;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nogaz on 28.05.2017.
 */
public class MopTwoImpl extends MopImpl {

    public MopTwoImpl(){
        super();
        this.filename = "mopTwo.txt";
    }

    @Override
    public void evaluate(Individual individual) {
        double characteristics[] = individual.getCoordinates();
        double evalue1;
        double evalue2;

        double sum1 = 0;
        double sum2 = 0;
        for( int i = 0 ; i < characteristics.length ; ++i){
            sum1 += Math.pow(characteristics[i] - 1/Math.sqrt(characteristics.length),2);
        }
        evalue1 = 1 - Math.exp(-sum1);

        for( int i = 0 ; i < characteristics.length ; ++i){
            sum2 += Math.pow(characteristics[i] + 1/Math.sqrt(characteristics.length),2);
        }
        evalue2 = 1 - Math.exp(-sum2);
        individual.setEvaluatedValues(0, evalue1);
        individual.setEvaluatedValues(1, evalue2);
        //this.terminationCondition--;
    }

    @Override
    public Individual generateIndividual() {
        double[] characteristic = new double[3];
        for (int i = 0; i < characteristic.length; ++i) {
            characteristic[i] = ThreadLocalRandom.current().nextInt(-4, 4 + 1);
        }
        Individual individual = new Individual(3, characteristic, 2);
        evaluate(individual);
        return individual;
    }
}
