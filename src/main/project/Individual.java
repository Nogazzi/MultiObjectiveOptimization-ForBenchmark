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
    private final double[] characteristics;
    private double[] criteriaValues;
    private int dominanceDepth = 0;
    private int dominanceRank = 1;
    private int dominanceCount = 0;
    private Set<Individual> dominators = new TreeSet<>();
    private Set<Individual> dominated = new TreeSet<>();

    public Individual(final int size) {
        this.size = size;
        this.characteristics = new double[size];
        this.criteriaValues = new double[size];
        for (int i = 0; i < size; ++i) {
            double newDouble = new Random().nextDouble();

            characteristics[i] = newDouble;
        }
    }

    public Individual(final double[] characteristics, final int size) {
        this.size = size;
        this.characteristics = characteristics;
    }

    public Individual(final int size, final double x1, final double x2) {
        this.size = size;
        this.characteristics = new double[size];
        this.characteristics[0] = x1;
        this.characteristics[1] = x2;
    }

    public Individual(final int size, final double[] characteristics) {
        this.size = size;
        this.characteristics = characteristics;
    }

    public Individual(final int size, final double[] characteristics, final int criteriasAmount){
        this.size = size;
        this.characteristics = characteristics;
        this.criteriaValues = new double[criteriasAmount];
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

    public void setCriteriaValues(int index, double value){
        this.criteriaValues[index] = value;
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

    public double getCharacteristic(final int index) {
        return this.characteristics[index];
    }

    public double[] getCharacteristics(){
        return this.characteristics;
    }

    public boolean dominates(Individual o1) {
        if (o1.getSize() != getSize()) {

        }
        boolean dominates = false;
        for (int i = 0; i < o1.getSize(); ++i) {
            if (this.getCharacteristic(i) > o1.getCharacteristic(i)) {
                return false;
            } else if (this.getCharacteristic(i) < o1.getCharacteristic(i)) {
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
        int dif = Double.compare(o.getCharacteristic(0), getCharacteristic(0));
        if (dif == 0) {
            return Double.compare(o.getCharacteristic(1), getCharacteristic(1));
        }
        return dif;
    }

    @Override
    public String toString() {
        String desc = "[";
        for (int i = 0; i < size; ++i) {
            desc += this.characteristics[i];
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
            if (getCharacteristic(i) != o1.getCharacteristic(i)) {
                return false;
            }
        }
        return true;
    }

    public String printIndividual() {
        String result = "";
        for (int i = 0; i < size; ++i) {
            result += characteristics[i] + "\t";
        }
        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}