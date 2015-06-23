package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.exception.InvalidInputException;
import isula.aco.problems.flowshop.FlowShopProblemSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    final double[][] graph = this.problemSolver.getEnvironment()
        .getProblemGraph();

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        SchedulingFrame frame = new SchedulingFrame();
        frame.setSolutionMakespan(problemSolver.getBestSolutionQuality());

        frame.setProblemGraph(graph);
        frame.setSolution(problemSolver.getBestSolution());
        frame.setVisible(true);

      }
    });
  }

  /**
   * Solves a Flow-Shop instance using Ant Colony Optimization.
   * 
   * @return Array representing a solution.
   */
  public void solveProblem() {

    // TODO(cgavidia): Temporary fix. This should go on a pheromone start
    // routine.

    this.problemSolver.solveProblem();
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