package model.ea.operators.selection;

import helper.ArrayHelper;
import helper.ConfigurationHelper;
import helper.RandomHelper;
import model.ea.Population;

import java.util.ArrayList;
import java.util.List;

public class TournamentMatingSelection implements IMatingSelection {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("ea.NumberOfParents");
	private int numberOfDirectDuels = 3;//ConfigurationHelper.getInstance().getPropertyInteger("ea.NumberOfDirectDuels", 3);
	
	/**
     * @param population: the initial population of the generation
     * @return selection: a list of the selected individuals to become parents
     * Algorithm matches the q-times Tournament Selection
     */
	public Population select(Population population) {
		Population selection = new Population();
		List<Integer> usedIndices = new ArrayList<Integer>();

		for (int i = 0; i < numberOfParents; i++) {
		    int index;
		    do {
                index = RandomHelper.getInstance().getInt(population.getPool().size());
            } while (usedIndices.contains(index));
		    usedIndices.add(index);

			for (int j = 2; j < numberOfDirectDuels ; j++) {
			    int u;
			    do {
                    u = RandomHelper.getInstance().getInt(population.getPool().size());
                } while (usedIndices.contains(u));

			    if (population.getPool().get(u).getFitness() < population.getPool().get(index).getFitness()) {
                    ArrayHelper.getInstance().removeValue(usedIndices, index);
                    index = u;
                    usedIndices.add(index);
				}

				selection.addIndividualToPool(population.getPool().get(index));
			}
		}

		return selection;
	}
}
