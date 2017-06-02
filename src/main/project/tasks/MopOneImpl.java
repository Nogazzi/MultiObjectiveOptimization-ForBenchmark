package main.project.tasks;

import main.project.Individual;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nogaz on 28.05.2017.
 */
public class MopOneImpl extends MopImpl {

    public MopOneImpl(){
        super();

        this.filename = "mopOne.txt";
    }

    @Override
    public void evaluate(Individual individual) {
        double characteristic = individual.getCoordinate(0);
        individual.setEvaluatedValues(0, characteristic*characteristic);
        individual.setEvaluatedValues(1, Math.pow(characteristic-2,2));
        //this.terminationCondition--;
    }

    @Override
    public Individual generateIndividual() {
        double[] characteristic = {new Random().nextDouble()*10000};
        Individual individual = new Individual(1, characteristic, 2);
        evaluate(individual);
        return individual;
    }


}
