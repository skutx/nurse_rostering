package model.ea.operators.enviromentSelection;

import helper.ConfigurationHelper;
import model.ea.Population;

/**
 * Implements a simple environmental selection operator.
 */
public class SimpleEnvironmentSelection implements IEnvironmentSelection {
    /**
     * Number of individuals per population.
     */
    private int individualsPerPopulation = ConfigurationHelper.getInstance().getPropertyInteger("ea.IndividualsPerPopulation");

    /**
     * Selects the fittest individuals from a population.
     * @param population Old generation Population instance
     */
    @Override
    public void select(Population population) {
        // If there are less or equal individuals in this population than
        // configured, just abort.
        if (population.getPool().size() <= individualsPerPopulation) {
            return;
        }

        // sort population by fitness and remove individuals with the least fitness
        population.sortByFitness();
        for (int i = population.getPool().size() - 1; i >= individualsPerPopulation; i--) {
            population.getPool().remove(i);
        }
    }
}
