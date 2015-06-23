package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.exception.InvalidInputException;
import isula.aco.problems.flowshop.AntForFlowShop;
import isula.aco.problems.flowshop.FlowShopEnvironment;
import isula.aco.problems.flowshop.FlowShopProblemSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.UnsupportedLookAndFeelException;

import pe.edu.pucp.ia.aco.config.ProblemConfiguration;
import pe.edu.pucp.ia.aco.view.SchedulingFrame;

/**
 * Appies the MAX-MIN Ant System algorithm to Flow-Shop Problem instance.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrián Pareja (adrian@pareja.com)
 * 
 */
public class ACOFlowShop {

  public int[] bestTour;
  String bestScheduleAsString = "";
  public double bestScheduleMakespan = -1.0;

  private AcoProblemSolver problemSolver;

  public ACOFlowShop(double[][] graph) throws InvalidInputException {

    this.problemSolver = new FlowShopProblemSolver(graph,
        new ProblemConfiguration());
  }

  public static void main(String... args) {
    System.out.println("ACO FOR FLOW SHOP SCHEDULLING");
    System.out.println("=============================");

    try {
      String fileDataset = ProblemConfiguration.FILE_DATASET;
      System.out.println("Data file: " + fileDataset);

      // TODO(cgavidia): Maybe an interface here or an utility, to produce graph
      // from files.
      double[][] graph = getProblemGraphFromFile(fileDataset);
      ACOFlowShop acoFlowShop = new ACOFlowShop(graph);
      System.out.println("Starting computation at: " + new Date());
      long startTime = System.nanoTime();
      acoFlowShop.solveProblem();
      long endTime = System.nanoTime();
      System.out.println("Finishing computation at: " + new Date());
      System.out.println("Duration (in seconds): "
          + ((double) (endTime - startTime) / 1000000000.0));
      acoFlowShop.showSolution();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showSolution() throws ClassNotFoundException,
      InstantiationException, IllegalAccessException,
      UnsupportedLookAndFeelException {
    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
        .getInstalledLookAndFeels()) {
      if ("Nimbus".equals(info.getName())) {
        javax.swing.UIManager.setLookAndFeel(info.getClassName());
        break;
      }
    }

    // TODO(cgavidia): Doesn't seem like a clean way, but it will do the job.
    final double[][] graph = getGraph();

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        SchedulingFrame frame = new SchedulingFrame();
        frame.setSolutionMakespan(bestScheduleMakespan);

        frame.setProblemGraph(graph);
        frame.setSolution(bestTour);
        frame.setVisible(true);

      }
    });
  }

  // TODO(cgavidia): Temporary fix methods. We shouldn't expose the data
  // structures here.
  public double[][] getGraph() {
    return this.problemSolver.getEnvironment().getProblemGraph();
  }

  public int getNumberOfJobs() {
    return ((FlowShopEnvironment) this.problemSolver.getEnvironment())
        .getNumberOfJobs();
  }

  public double[][] getPheromoneTrails() {
    return this.problemSolver.getEnvironment().getPheromoneMatrix();
  }

  public AntForFlowShop[] getAntColony() {
    // TODO(cgavidia): Dirty fix to get this working

    return Arrays.copyOf(this.problemSolver.getAntColony().getHive(),
        this.problemSolver.getAntColony().getHive().length,
        AntForFlowShop[].class);
  }

  /**
   * Solves a Flow-Shop instance using Ant Colony Optimization.
   * 
   * @return Array representing a solution.
   */
  public int[] solveProblem() {
    System.out.println("INITIALIZING PHEROMONE MATRIX");
    double initialPheromoneValue = ProblemConfiguration.MAXIMUM_PHEROMONE;
    System.out.println("Initial pheromone value: " + initialPheromoneValue);

    // TODO(cgavidia): Temporary fix. This should go on a pheromone start
    // routine.
    this.problemSolver.applyInitialConfigurationPolicies();

    int iteration = 0;
    System.out.println("STARTING ITERATIONS");
    System.out.println("Number of iterations: "
        + ProblemConfiguration.MAX_ITERATIONS);

    while (iteration < ProblemConfiguration.MAX_ITERATIONS) {
      System.out.println("Current iteration: " + iteration);
      clearAntSolutions();
      buildSolutions();
      updatePheromoneTrails();
      updateBestSolution();
      iteration++;
    }
    System.out.println("EXECUTION FINISHED");
    System.out.println("Best schedule makespam: " + bestScheduleMakespan);
    System.out.println("Best schedule:" + bestScheduleAsString);
    return bestTour.clone();
  }

  /**
   * Updates pheromone trail values
   */
  private void updatePheromoneTrails() {
    this.problemSolver.applyAfterSolutionConstructionPolicies();
  }

  /**
   * Build a solution for every Ant in the Colony.
   */
  private void buildSolutions() {
    System.out.println("BUILDING ANT SOLUTIONS");
    int antCounter = 0;
    for (AntForFlowShop ant : getAntColony()) {
      System.out.println("Current ant: " + antCounter);
      while (ant.getCurrentIndex() < getNumberOfJobs()) {
        ant.selectNextNode(this.problemSolver.getEnvironment(),
            new ProblemConfiguration());
      }
      System.out.println("Original Solution > Makespan: "
          + ant.getSolutionQuality(this.problemSolver.getEnvironment())
          + ", Schedule: " + ant.getSolutionAsString());
      ant.improveSolution(this.problemSolver.getEnvironment());
      System.out.println("After Local Search > Makespan: "
          + ant.getSolutionQuality(this.problemSolver.getEnvironment())
          + ", Schedule: " + ant.getSolutionAsString());
      antCounter++;
    }
  }

  /**
   * Clears solution build for every Ant in the colony.
   */
  private void clearAntSolutions() {
    System.out.println("CLEARING ANT SOLUTIONS");
    for (AntForFlowShop ant : getAntColony()) {
      ant.setCurrentIndex(0);
      ant.clear();
    }
  }

  /**
   * Returns the best performing Ant in Colony
   * 
   * @return The Best Ant
   */
  private AntForFlowShop getBestAnt() {
    // TODO(cgavidia): Very ugly hack while refactoring
    return (AntForFlowShop) this.problemSolver.getAntColony()
        .getBestPerformingAnt(this.problemSolver.getEnvironment());
  }

  /**
   * Selects the best solution found so far.
   * 
   * @return
   */
  private void updateBestSolution() {
    System.out.println("GETTING BEST SOLUTION FOUND");
    AntForFlowShop bestAnt = getBestAnt();
    if (bestTour == null
        || bestScheduleMakespan > bestAnt.getSolutionQuality(this.problemSolver
            .getEnvironment())) {
      bestTour = bestAnt.getSolution().clone();
      bestScheduleMakespan = bestAnt.getSolutionQuality(this.problemSolver
          .getEnvironment());
      bestScheduleAsString = bestAnt.getSolutionAsString();
    }
    System.out.println("Best solution so far > Makespan: "
        + bestScheduleMakespan + ", Schedule: " + bestScheduleAsString);
  }

  /**
   * 
   * Reads a text file and returns a problem matrix.
   * 
   * @param path
   *          File to read.
   * @return Problem matrix.
   * @throws IOException
   */
  public static double[][] getProblemGraphFromFile(String path)
      throws IOException {
    double graph[][] = null;
    FileReader fr = new FileReader(path);
    BufferedReader buf = new BufferedReader(fr);
    String line;
    int i = 0;

    while ((line = buf.readLine()) != null) {
      if (i > 0) {
        String splitA[] = line.split(" ");
        LinkedList<String> split = new LinkedList<String>();
        for (String s : splitA) {
          if (!s.isEmpty()) {
            split.add(s);
          }
        }
        int j = 0;
        for (String s : split) {
          if (!s.isEmpty()) {
            graph[i - 1][j++] = Integer.parseInt(s);
          }
        }
      } else {
        String firstLine[] = line.split(" ");
        String numberOfJobs = firstLine[0];
        String numberOfMachines = firstLine[1];

        if (graph == null) {
          graph = new double[Integer.parseInt(numberOfJobs)][Integer
              .parseInt(numberOfMachines)];
        }
      }
      i++;
    }

    buf.close();
    return graph;
  }
}