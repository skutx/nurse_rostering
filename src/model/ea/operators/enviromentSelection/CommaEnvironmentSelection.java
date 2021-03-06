package model.ea.operators.enviromentSelection;

import helper.ConfigurationHelper;
import model.Individual;
import model.ea.Population;

import java.util.ArrayList;
import java.util.List;

public class CommaEnvironmentSelection implements IEnvironmentSelection {
	 private int individualsPerPopulation = ConfigurationHelper.getInstance().getPropertyInteger("ea.IndividualsPerPopulation");
	 
	 /** 
	  *@param population - takes the population after Recombination and Mutation
	  *selects the number of needed best individuals from children only
	  *requires that the number of children is at least as big as individuals needed
	  *@return thereby the new Generation of the population is created
      */
	@Override
	public void select(Population population) {
		List<Individual> children = population.getPool().subList(individualsPerPopulation, population.getPool().size());
		List <Individual> newGeneration = new ArrayList<Individual>();
		children.sort((individualA, individualB) -> Float.compare(individualA.getFitness(), individualB.getFitness()));
		for (int i = 0; i < individualsPerPopulation; i++) {
			if(children.isEmpty()){break;}
			newGeneration.add(children.get(i));
		}
		population.getPool().clear();
		population.getPool().addAll(newGeneration);
	}

}
