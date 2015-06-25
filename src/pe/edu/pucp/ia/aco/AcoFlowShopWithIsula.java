package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixPolicy;
import isula.aco.algorithms.maxmin.UpdatePheromoneMatrixPolicy;
import isula.aco.problems.flowshop.FlowShopProblemSolver;
import isula.aco.problems.flowshop.LocalSearchPolicy;

import pe.edu.pucp.ia.aco.config.ProblemConfiguration;
import pe.edu.pucp.ia.aco.view.SchedulingFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * Appies the MAX-MIN Ant System algorithm to Flow-Shop Problem instance.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrian Pareja (adrian@pareja.com)
 */
public class AcoFlowShopWithIsula {

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

      // TODO(cgavidia): Maybe an interface here or an utility, to produce graph
      // from files.
      double[][] graph = getProblemGraphFromFile(fileDataset);
      System.out.println("Data file: " + fileDataset);

      AcoProblemSolver problemSolver;

      ProblemConfiguration configurationProvider = new ProblemConfiguration();
      problemSolver = new FlowShopProblemSolver(graph, configurationProvider);

      problemSolver.addDaemonAction(new StartPheromoneMatrixPolicy());
      problemSolver.addDaemonAction(new UpdatePheromoneMatrixPolicy());

      Ant[] hive = problemSolver.getAntColony().getHive();
      for (Ant ant : hive) {
        ant.addPolicy(new PseudoRandomNodeSelection());
        ant.addPolicy(new LocalSearchPolicy());
      }

      problemSolver.solveProblem();
      showSolution(graph, problemSolver);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void showSolution(final double[][] graph,
      final AcoProblemSolver problemSolver) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException,
      UnsupportedLookAndFeelException {
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