package pe.edu.pucp.ia.aco.config;

import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;
import isula.aco.exception.ConfigurationException;

/**
 * Parameter settings for the algorithm.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrian Pareja (adrian@pareja.com)
 */
public class ProblemConfiguration implements MaxMinConfigurationProvider,
    AcsConfigurationProvider {

  private static final String FILE_NAME = "flowshop_75x20.data";
  private static final String FILE_FOLDER = 
      "C:/Users/CarlosG/Documents/GitHub/ACOFlowShopWithIsula/src/";

  public static final String FILE_DATASET = FILE_FOLDER + FILE_NAME;

  private static final int NUMBER_OF_ANTS = 1;

  // TODO(cgavidia): I believe this parameters are used in another ACO
  // algorithms, or for the TSP Problem.
  // private static final double ALPHA = 1;
  // private static final double BETA = 5;
  private static final double EVAPORATION = 0.5;
  private static final int Q = 1;
  private static final double MAXIMUM_PHEROMONE = 1.0;
  private static final int MAX_ITERATIONS = 20000;

  public int getNumberOfAnts() {
    return NUMBER_OF_ANTS;
  }

  public double getMaximumPheromoneValue() {
    return MAXIMUM_PHEROMONE;
  }

  public double getEvaporationRatio() {
    return EVAPORATION;
  }

  public double getMinimumPheromoneValue() {
    return MAXIMUM_PHEROMONE / 5;
  }

  public double getQValue() {
    return Q;
  }

  public int getBestChoiceConstant() {
    return 4;
  }

  public int getNumberOfIterations() {
    return MAX_ITERATIONS;
  }

  public double getInitialPheromoneValue() {
    throw new ConfigurationException(
        "We don't use this parameter since we're using Max-Min Ant System");
  }
}
