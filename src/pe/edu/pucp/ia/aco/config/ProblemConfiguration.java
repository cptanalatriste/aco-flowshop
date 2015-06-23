package pe.edu.pucp.ia.aco.config;

import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;

/**
 * Parameter settings for the algorithm.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrián Pareja (adrian@pareja.com)
 */
public class ProblemConfiguration implements MaxMinConfigurationProvider,
    AcsConfigurationProvider {

  // public static final String FILE_DATASET =
  // "C:/Users/V144615/Documents/GitHub/ACOFlowShopWithIsula/src/flowshop_75x20.data";
  public static final String FILE_DATASET = "C:/Users/CarlosG/Documents/GitHub/ACOFlowShopWithIsula/src/flowshop_75x20.data";

  public static final int NUMBER_OF_ANTS = 1;
  public static final double ALPHA = 1;
  public static final double BETA = 5;
  public static final double EVAPORATION = 0.5;
  public static final int Q = 1;
  public static final double MAXIMUM_PHEROMONE = 1.0;
  public static final double MINIMUM_PHEROMONE = MAXIMUM_PHEROMONE / 5;
  public static final int MAX_ITERATIONS = 20000;

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
}
