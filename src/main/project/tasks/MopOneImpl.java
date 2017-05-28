package main.project.tasks;

import main.project.Individual;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nogaz on 28.05.2017.
 */
public class MopOneImpl extends MopImpl {


    @Override
    public void evaluate(Individual individual) {
        double characteristic = individual.getCharacteristic(0);
        individual.setCriteriaValues(0, characteristic*characteristic);
        individual.setCriteriaValues(1, Math.pow(characteristic-2,2));
        this.terminationCondition--;
    }

    @Override
    public Individual generateIndividual() {
        double[] characteristic = {ThreadLocalRandom.current().nextInt(-100000, 100000 + 1)};
        Individual individual = new Individual(1, characteristic, 2);
        evaluate(individual);
        return individual;
    }
}
