package main.project;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Nogaz on 15.05.2017.
 */
public class Individual implements Comparable<Individual> {

    private final int size;
    private final double[] coordinates;
    private double[] evaluatedValues;
    private int dominanceDepth = 0;
    private int dominanceRank = 1;
    private int dominanceCount = 0;
    private Set<Individual> dominators = new TreeSet<>();
    private Set<Individual> dominated = new TreeSet<>();
    private double crowdingSortValue = 0;

    public Individual(final int size) {
        this.size = size;
        this.coordinates = new double[size];
        this.evaluatedValues = new double[size];
        for (int i = 0; i < size; ++i) {
            double newDouble = new Random().nextDouble();

            coordinates[i] = newDouble;
        }
    }

    public Individual(final double[] characteristics, final int size) {
        this.size = size;
        this.coordinates = characteristics;
    }

    public Individual(final int size, final double x1, final double x2) {
        this.size = size;
        this.coordinates = new double[size];
        this.coordinates[0] = x1;
        this.coordinates[1] = x2;
    }

    public Individual(final int size, final double[] characteristics) {
        this.size = size;
        this.coordinates = characteristics;
        this.evaluatedValues = new double[2];
    }

    public Individual(final int size, final double[] characteristics, final int criteriasAmount){
        this.size = size;
        this.coordinates = characteristics;
        this.evaluatedValues = new double[criteriasAmount];
    }

    public void setDominanceDepth(int dominanceDepth) {
        this.dominanceDepth = dominanceDepth;
    }

    public void setDominanceRank(int dominanceRank) {
        this.dominanceRank = dominanceRank;
    }

    public void setDominanceCount(int dominanceCount) {
        this.dominanceCount = dominanceCount;
    }

    public void setCrowdingSortValue(double distance){
        this.crowdingSortValue = distance;
    }

    public void setEvaluatedValues(int index, double value){
        this.evaluatedValues[index] = value;
    }

    public double getCrowdingSortValue(){
        return this.crowdingSortValue;
    }

    public int getDominanceDepth() {
        return dominanceDepth;
    }

    public int getDominanceRank() {
        return this.dominators.size() + 1;
    }

    public int getDominanceCount() {
        return this.dominated.size();
    }

    public void increaseDominanceRank() {
        this.dominanceRank++;
    }

    public void addDominator(Individual dominator) {
        this.dominators.add(dominator);
    }

    public void addDominated(Individual dominated) {
        this.dominated.add(dominated);
    }

    public int getSize() {
        return this.size;
    }

    public double getCoordinate(final int index) {
        return this.coordinates[index];
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public double[] getEvaluatedValues() {
        return evaluatedValues;
    }

    public double getEvaluatedValue(int i) {
        return evaluatedValues[i];
    }

    public boolean dominates(Individual o1) {
        if (o1.getSize() != getSize()) {

        }
        boolean dominates = false;
        for (int i = 0; i < o1.getSize(); ++i) {
            if (this.getCoordinate(i) > o1.getCoordinate(i)) {
                return false;
            } else if (this.getCoordinate(i) < o1.getCoordinate(i)) {
                dominates = true;
            }
        }
        if (dominates) {
            o1.addDominator(this);
            this.addDominated(o1);
        }
        return dominates;
    }


    public boolean dominatedByAnyFrom(List<Individual> population) {
        for (int i = 0; i < population.size(); ++i) {
            if (population.get(i).dominates(this)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public int compareTo(Individual o) {
        int dif = Double.compare(o.getCoordinate(0), getCoordinate(0));
        if (dif == 0) {
            return Double.compare(o.getCoordinate(1), getCoordinate(1));
        }
        return dif;
    }

    @Override
    public String toString() {
        String desc = "[";
        for (int i = 0; i < size; ++i) {
            desc += this.coordinates[i];
            if (i != size - 1) {
                desc += ";";
            }
        }
        desc += "]";
        return desc;
    }

    @Override
    public boolean equals(Object obj) {
        Individual o1 = (Individual) obj;
        if (this.getSize() != o1.getSize()) {
            return false;
        }
        for (int i = 0; i < getSize(); ++i) {
            if (getCoordinate(i) != o1.getCoordinate(i)) {
                return false;
            }
        }
        return true;
    }

    public String printIndividual() {
        String result = "";
        for (int i = 0; i < size; ++i) {
            result += coordinates[i] + "\t";
        }
        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}