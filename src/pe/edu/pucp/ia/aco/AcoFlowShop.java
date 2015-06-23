package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.exception.InvalidInputException;
import isula.aco.problems.flowshop.FlowShopProblemSolver;

import pe.edu.pucp.ia.aco.config.ProblemConfiguration;
import pe.edu.pucp.ia.aco.view.SchedulingFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * Appies the MAX-MIN Ant System algorithm to Flow-Shop Problem instance.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrián Pareja (adrian@pareja.com)
 * 
 */
public class AcoFlowShop {

  private AcoProblemSolver problemSolver;

  /**
   * Generates an AcoFlowShop instance.
   * 
   * @param graph
   *          Graph representation of the problem.
   * @throws InvalidInputException
   *           Generated if the graph is not correct.
   */
  public AcoFlowShop(double[][] graph) throws InvalidInputException {

    this.problemSolver = new FlowShopProblemSolver(graph,
        new ProblemConfiguration());
  }

  /**
   * Entry point for this solution.
   * 
   * @param args
   *          Arguments for the application.
   */
  public static void main(String... args) {
    System.out.println("ACO FOR FLOW SHOP SCHEDULLING");
    System.out.println("=============================");

    try {
      String fileDataset = ProblemConfiguration.FILE_DATASET;
      System.out.println("Data file: " + fileDataset);

      // TODO(cgavidia): Maybe an interface here or an utility, to produce graph
      // from files.
      double[][] graph = getProblemGraphFromFile(fileDataset);
      AcoFlowShop acoFlowShop = new AcoFlowShop(graph);
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
   */
  public void solveProblem() {

    // TODO(cgavidia): Temporary fix. This should go on a pheromone start
    // routine.

    this.problemSolver.solveProblem();
  }

  /**
   * Reads a text file and returns a problem matrix.
   * 
   * @param path
   *          File to read.
   * @return Problem matrix.
   * @throws IOException
   *           If an error produces while reading the file.
   */
  public static double[][] getProblemGraphFromFile(String path)
      throws IOException {
    double[][] graph = null;
    FileReader fr = new FileReader(path);
    BufferedReader buf = new BufferedReader(fr);
    String line;
    int index = 0;

    while ((line = buf.readLine()) != null) {
      if (index > 0) {
        String[] splitA = line.split(" ");
        LinkedList<String> split = new LinkedList<String>();
        for (String s : splitA) {
          if (!s.isEmpty()) {
            split.add(s);
          }
        }
        int column = 0;
        for (String anString : split) {
          if (!anString.isEmpty()) {
            graph[index - 1][column++] = Integer.parseInt(anString);
          }
        }
      } else {
        String[] firstLine = line.split(" ");
        String numberOfJobs = firstLine[0];
        String numberOfMachines = firstLine[1];

        if (graph == null) {
          graph = new double[Integer.parseInt(numberOfJobs)][Integer
              .parseInt(numberOfMachines)];
        }
      }
      index++;
    }

    buf.close();
    return graph;
  }
}