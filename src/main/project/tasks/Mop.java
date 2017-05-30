package main.project.tasks;

import main.project.Individual;

import java.util.List;

/**
 * Created by Nogaz on 28.05.2017.
 */
public interface Mop {
    public void evaluate(Individual individual);
    public Individual generateIndividual();
    public void doSimulation();
    public List<Individual> generatePopulation();
    public void/*List<Individual>*/ crowdingSort(List<Individual> individuals);
    //public void identifyFronts(List<Individual> individuals);
    public void calculateCrowdDistance(List<Individual> individuals);
}
