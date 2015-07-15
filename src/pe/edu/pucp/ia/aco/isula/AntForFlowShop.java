package pe.edu.pucp.ia.aco.isula;

import isula.aco.Ant;

import java.util.ArrayList;
import java.util.List;

/**
 * An Ant that belongs to Colony in the context of ACO.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrian Pareja (adrian@pareja.com)
 */
public class AntForFlowShop extends Ant<Integer, FlowShopEnvironment> {

  private static final double VALUE_NOT_USED = 1.0;

  // TODO(cgavidia): Maybe a more generic parameter would be an Environment
  // instance.
  /**
   * Creates an Ant specialized in the Flow Shop Scheduling problem.
   * 
   * @param graphLenght
   *          Number of rows of the Problem Graph.
   */
  public AntForFlowShop(int graphLenght) {
    super();
    this.setSolution(new Integer[graphLenght]);
    this.setVisited(new ArrayList<Integer>());
  }

  @Override
  public boolean isSolutionReady(FlowShopEnvironment environment) {
    return getCurrentIndex() == environment.getNumberOfJobs();
  }

  /**
   * Gets the makespan of the Ants built solution.
   * 
   * @param graph
   *          Problem graph.
   * @return Makespan of the solution.
   */
  @Override
  public double getSolutionQuality(FlowShopEnvironment environment) {
    return getScheduleMakespan(getSolution(), environment.getProblemGraph());
  }

  @Override
  public Double getHeuristicValue(Integer solutionComponent,
      Integer positionInSolution, FlowShopEnvironment environment) {
    return VALUE_NOT_USED;
  }

  @Override
  public List<Integer> getNeighbourhood(FlowShopEnvironment environment) {
    List<Integer> neighbours = new ArrayList<Integer>();
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();

    for (int l = 0; l < pheromoneMatrix.length; l++) {
      neighbours.add(l);
    }
    return neighbours;
  }

  @Override
  public Double getPheromoneTrailValue(Integer solutionComponent,
      Integer positionInSolution, FlowShopEnvironment environment) {

    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    return pheromoneMatrix[solutionComponent][positionInSolution];
  }

  @Override
  public void setPheromoneTrailValue(Integer solutionComponent,
      FlowShopEnvironment environment, Double value) {
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();

    pheromoneMatrix[solutionComponent][solutionComponent] = value;
  }

  /**
   * Calculates the MakeSpan for the generated schedule.
   * 
   * @param schedule
   *          Schedule
   * @param jobInfo
   *          Job Info.
   * @return Makespan.
   */
  public double getScheduleMakespan(Integer[] schedule, double[][] jobInfo) {
    int machines = jobInfo[0].length;
    double[] machinesTime = new double[machines];
    double tiempo = 0;

    for (Integer job : schedule) {
      for (int i = 0; i < machines; i++) {
        tiempo = jobInfo[job][i];
        if (i == 0) {
          machinesTime[i] = machinesTime[i] + tiempo;
        } else {
          if (machinesTime[i] > machinesTime[i - 1]) {
            machinesTime[i] = machinesTime[i] + tiempo;
          } else {
            machinesTime[i] = machinesTime[i - 1] + tiempo;
          }
        }
      }
    }
    return machinesTime[machines - 1];
  }

}