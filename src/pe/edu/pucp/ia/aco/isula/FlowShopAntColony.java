package pe.edu.pucp.ia.aco.isula;

import isula.aco.Ant;
import isula.aco.AntColony;

public class FlowShopAntColony extends AntColony<Integer, FlowShopEnvironment> {

  private int numberOfJobs;

  /**
   * Creates an Ant Colony to solve the Flow Shop Scheduling problem.
   * 
   * @param numberOfAnts
   *          Ants for the colony.
   * @param numberOfJobs
   *          Number of jobs.
   */
  public FlowShopAntColony(int numberOfAnts, int numberOfJobs) {
    super(numberOfAnts);

    this.numberOfJobs = numberOfJobs;
  }

  @Override
  protected Ant<Integer, FlowShopEnvironment> createAnt(
      FlowShopEnvironment environment) {
    AntForFlowShop ant = new AntForFlowShop(numberOfJobs);
    return ant;
  }
}
