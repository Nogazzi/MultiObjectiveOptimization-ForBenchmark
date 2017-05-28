package main.project.tasks;

import main.project.Individual;

/**
 * Created by Nogaz on 28.05.2017.
 */
public interface Mop {
    public void evaluate(Individual individual);
    public Individual generateIndividual();
    public void doSimulation();
    public void generatePopulation();
}
