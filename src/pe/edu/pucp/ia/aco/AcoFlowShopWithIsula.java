package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixForMaxMin;
import isula.aco.problems.flowshop.ApplyLocalSearch;
import isula.aco.problems.flowshop.FlowShopProblemSolver;

import pe.edu.pucp.ia.aco.config.ProblemConfiguration;
import pe.edu.pucp.ia.aco.isula.FlowShopUpdatePheromoneMatrix;
import pe.edu.pucp.ia.aco.view.SchedulingFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * Appies the MAX-MIN Ant System algorithm to Flow-Shop Problem instance.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrian Pareja (adrian@pareja.com)
 */
public class AcoFlowShopWithIsula {

  private static Logger logger = Logger.getLogger(AcoFlowShopWithIsula.class
      .getName());

  /**
   * Entry point for this solution.
   * 
   * @param args
   *          Arguments for the application.
   */
  public static void main(String... args) {
    logger.info("ACO FOR FLOW SHOP SCHEDULLING");
    logger.info("=============================");

    try {
      String fileDataset = ProblemConfiguration.FILE_DATASET;

      // TODO(cgavidia): Maybe an interface here or an utility, to produce graph
      // from files.
      double[][] graph = getProblemGraphFromFile(fileDataset);
      logger.info("Data file: " + fileDataset);

      FlowShopProblemSolver problemSolver;

      ProblemConfiguration configurationProvider = new ProblemConfiguration();
      problemSolver = new FlowShopProblemSolver(graph, configurationProvider);
      configurationProvider.setEnvironment(problemSolver.getEnvironment());

      problemSolver
          .addDaemonAction(new StartPheromoneMatrixForMaxMin<Integer>());
      problemSolver.addDaemonAction(new FlowShopUpdatePheromoneMatrix());

      List<Ant<Integer>> hive = problemSolver.getAntColony().getHive();
      for (Ant<Integer> ant : hive) {
        ant.addPolicy(new PseudoRandomNodeSelection<Integer>());
        ant.addPolicy(new ApplyLocalSearch());
      }

      problemSolver.solveProblem();
      showSolution(graph, problemSolver);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void showSolution(final double[][] graph,
      final AcoProblemSolver<Integer> problemSolver)
      throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, UnsupportedLookAndFeelException {
    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
        .getInstalledLookAndFeels()) {
      if ("Nimbus".equals(info.getName())) {
        javax.swing.UIManager.setLookAndFeel(info.getClassName());
        break;
      }
    }

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

        logger.info("numberOfJobs=" + numberOfJobs + ", numberOfMachines="
            + numberOfMachines);

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