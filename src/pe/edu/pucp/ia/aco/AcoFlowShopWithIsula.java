package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixForMaxMin;
import pe.edu.pucp.ia.aco.config.ProblemConfiguration;
import pe.edu.pucp.ia.aco.isula.*;
import pe.edu.pucp.ia.aco.view.SchedulingFrame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
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
     * @param args Arguments for the application.
     */
    @SuppressWarnings("unchecked")
    public static void main(String... args) {
        logger.info("ACO FOR FLOW SHOP SCHEDULLING");
        logger.info("=============================");

        try {
            String fileDataset = ProblemConfiguration.FILE_DATASET;

            // TODO(cgavidia): Maybe an interface here or an utility, to produce graph from files.
//            double[][] graph = getProblemGraphFromFile(fileDataset);
            double[][] problemRepresentation = getTaillardProblemFromFile(fileDataset, 20, 5);
            logger.info("Data file: " + fileDataset);

            FlowShopProblemSolver problemSolver;

            ProblemConfiguration configurationProvider = new ProblemConfiguration(problemRepresentation);
            problemSolver = new FlowShopProblemSolver(problemRepresentation, configurationProvider);
            configurationProvider.setEnvironment(problemSolver.getEnvironment());

            problemSolver.addDaemonActions(
                    new StartPheromoneMatrix<Integer, FlowShopEnvironment>(),
                    new FlowShopUpdatePheromoneMatrix());
            problemSolver.getAntColony().addAntPolicies(
                    new PseudoRandomNodeSelection<Integer, FlowShopEnvironment>(),
                    new ApplyLocalSearch());

            problemSolver.solveProblem();
            showSolution(problemRepresentation, problemSolver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showSolution(final double[][] graph,
                                     final AcoProblemSolver<Integer, FlowShopEnvironment> problemSolver)
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
                frame.setSolutionMakespan(problemSolver.getBestSolutionCost());

                frame.setProblemGraph(graph);
                frame.setSolution(problemSolver.getBestSolution());
                frame.setVisible(true);

            }
        });
    }

    public static double[][] getTaillardProblemFromFile(String fileName, int numberOfJobs,
                                                        int numberOfMachines) throws IOException {

        double[][] problemRepresentation = new double[numberOfJobs][numberOfMachines];
        int currentMachine = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(" ");

                if (tokens.length == numberOfJobs) {
                    for (int jobIndex = 0; jobIndex < tokens.length; jobIndex += 1) {
                        problemRepresentation[jobIndex][currentMachine] = Integer.parseInt(tokens[jobIndex]);
                    }
                    currentMachine += 1;
                }
            }
        }
        return problemRepresentation;
    }

    /**
     * Reads a text file and returns a problem matrix.
     *
     * @param path File to read.
     * @return Problem matrix.
     * @throws IOException If an error produces while reading the file.
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
                LinkedList<String> split = new LinkedList<>();
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