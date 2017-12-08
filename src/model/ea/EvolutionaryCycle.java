package model.ea;


import helper.ClassLoaderHelper;
import helper.ConfigurationHelper;
import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.ea.operators.enviromentSelection.IEnvironmentSelection;
import model.ea.operators.mutation.IMutation;
import model.ea.operators.recombination.IRecombination;
import model.ea.operators.selection.IMatingSelection;
import model.schedule.SchedulingPeriod;

/**
 * This class implements an evolutionary cycle for the evolutionary algorithm.
 */
public class EvolutionaryCycle {
    /**
     * Current iteration.
     */
    private int iteration = 0;

    /**
     * Maximum number of iterations.
     */
    private int maxIterations = ConfigurationHelper.getInstance().getPropertyInteger("ea.MaxIterations", 1000);

    /**
     * The recombination operator is used, if true.
     */
    private boolean useRecombination = ConfigurationHelper.getInstance().getPropertyBoolean("ea.UseRecombination");

    /**
     * The mutation operator is used, if true.
     */
    private boolean useMutation = ConfigurationHelper.getInstance().getPropertyBoolean("ea.UseMutation");

    /**
     * Holds the mating selection operator instance.
     */
    private IMatingSelection matingSelectionOperator = ClassLoaderHelper.getInstance().getMatingSelectionOperator();

    /**
     * Holds the recombination operator instance.
     */
    private IRecombination recombinationOperator = ClassLoaderHelper.getInstance().getRecombinationOperator();

    /**
     * Holds the mutation operator instance.
     */
    private IMutation mutationOperator = ClassLoaderHelper.getInstance().getMutationOperator();

    /**
     * Holds the environment selection operator instance.
     */
    private IEnvironmentSelection environmentSelectionOperator = ClassLoaderHelper.getInstance().getEnvironmentSelectionOperator();

    /**
     * Holds the initializing population (for benchmarking purposes against last solutions).
     */
    private Population initPopulation = null;

    /**
     * Returns the initializing population.
     * @return Initialization population instance
     */
    public Population getInitPopulation() {
        return initPopulation;
    }

    /**
     * Runs the evolutionary cycle.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    public Population evolutionize(SchedulingPeriod period) {
        initPopulation = generateInitializationPopulation(period);
        initPopulation.benchmark();

        Population population = Population.copy(initPopulation);
        Population children;

        // evolutionize cycle, while termination condition is not met
        while (!isTerminationCondition()) {
            // get selection of mating individuals
            Population parents = matingSelectionOperator.select(population);
            children = new Population();

            // recombine individuals (if used)
            if (useRecombination) {
                 children = recombinationOperator.recombine(parents);
            }

            // mutate individuals (if used)
            if (useMutation) {
            	// if recombination was used, mutate the new created children
            	if (useRecombination) {
            		children = mutationOperator.mutate(children);
            	} else {
                    // if not, mutate the selected parents
                    children = mutationOperator.mutate(parents);
            	}
            }

            // benchmark new generation
            population.addIndividualsToPool(children.getPool());
            population.benchmark();

            // get environmental selection from new generation
            environmentSelectionOperator.select(population);
            population.benchmark();
        }

        // cycle is terminated, return the latest solution or initialized
        // population, of solution is not better
        return population.getBestIndividual().getFitness()
                <= initPopulation.getBestIndividual().getFitness()
            ? population
            : initPopulation;
    }

    /**
     * Returns true, if the termination condition is met.
     * @return True, if termination condition is met
     */
    private boolean isTerminationCondition() {
        return ++iteration > maxIterations;
    }

    /**
     * Returns a generated initialization population for a specific scheduling period.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    private Population generateInitializationPopulation(SchedulingPeriod period) {
        Population population = new Population();

        int individualsPerPopulation = ConfigurationHelper.getInstance().getPropertyInteger("ea.IndividualsPerPopulation", 10);
        for (int i = 0; i < individualsPerPopulation; i++) {
            RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
            Individual individual = randomConstructionHeuristic.getIndividual(period);
            population.addIndividualToPool(individual);
        }

        return population;
    }
}
