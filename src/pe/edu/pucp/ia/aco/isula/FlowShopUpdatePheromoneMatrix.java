package pe.edu.pucp.ia.aco.isula;

import isula.aco.Ant;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;
import isula.aco.algorithms.maxmin.UpdatePheromoneMatrixForMaxMin;
import pe.edu.pucp.ia.aco.config.ProblemConfiguration;

public class FlowShopUpdatePheromoneMatrix extends
    UpdatePheromoneMatrixForMaxMin<Integer> {

  @Override
  protected double getNewPheromoneValue(Ant<Integer> bestAnt,
      int positionInSolution, Integer solutionComponent,
      MaxMinConfigurationProvider configurationProvider) {

    double contribution = ProblemConfiguration.Q
        / bestAnt.getSolutionQuality(getEnvironment());

    double newValue = bestAnt.getPheromoneTrailValue(solutionComponent,
        positionInSolution, getEnvironment()) + contribution;
    return newValue;
  }
}
