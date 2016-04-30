package isula.aco.flowshop;

import isula.aco.Ant;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;
import isula.aco.algorithms.maxmin.UpdatePheromoneMatrixForMaxMin;
import pe.edu.pucp.ia.aco.config.ProblemConfiguration;

public class FlowShopUpdatePheromoneMatrix extends
        UpdatePheromoneMatrixForMaxMin<Integer, FlowShopEnvironment> {

    @Override
    protected double getNewPheromoneValue(
            Ant<Integer, FlowShopEnvironment> ant, int positionInSolution,
            Integer solutionComponent,
            MaxMinConfigurationProvider configurationProvider) {

        double contribution = ProblemConfiguration.Q
                / ant.getSolutionCost(getEnvironment());

        double newValue = ant.getPheromoneTrailValue(solutionComponent,
                positionInSolution, getEnvironment()) + contribution;
        return newValue;
    }

    @Override
    protected double getMaximumPheromoneValue(MaxMinConfigurationProvider configurationProvider) {
        double bestSolutionCost = getProblemSolver().getBestSolutionCost();
        if (bestSolutionCost == 0) {
            return configurationProvider.getInitialPheromoneValue();
        }

        return 1 / (1 - configurationProvider.getEvaporationRatio()) / bestSolutionCost;
    }

    @Override
    protected double getMinimumPheromoneValue(MaxMinConfigurationProvider configurationProvider) {
        return this.getMaximumPheromoneValue(configurationProvider) / 5;
    }
}
