package pe.edu.pucp.ia.aco;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.AntColony;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.flowshop.AntForFlowShop;
import isula.aco.flowshop.ApplyLocalSearch;
import isula.aco.flowshop.FlowShopEnvironment;
import isula.aco.flowshop.FlowShopUpdatePheromoneMatrix;
import pe.edu.pucp.ia.aco.config.ProblemConfiguration;
import pe.edu.pucp.ia.aco.view.SchedulingFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Applies the MAX-MIN Ant System algorithm to Flow-Shop Problem instance.
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
    public static void main(String... args) {
        logger.info("ACO FOR FLOW SHOP SCHEDULLING");
        logger.info("=============================");

        try {
            String fileDataset = ProblemConfiguration.FILE_DATASET;

            double[][] problemRepresentation = getTaillardProblemFromFile(fileDataset, 20, 5);
            logger.info("Data file: " + fileDataset);

            ProblemConfiguration configurationProvider = new ProblemConfiguration(problemRepresentation);
            AntColony<Integer, FlowShopEnvironment> colony = getAntColony(configurationProvider);
            FlowShopEnvironment environment = new FlowShopEnvironment(problemRepresentation);
            configurationProvider.setEnvironment(environment);

            AcoProblemSolver<Integer, FlowShopEnvironment> solver = new AcoProblemSolver<>();
            solver.initialize(environment, colony, configurationProvider);

            solver.addDaemonActions(
                    new StartPheromoneMatrix<>(),
                    new FlowShopUpdatePheromoneMatrix());
            solver.getAntColony().addAntPolicies(
                    new PseudoRandomNodeSelection<>(),
                    new ApplyLocalSearch());

            solver.solveProblem();
            showSolution(problemRepresentation, solver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AntColony<Integer, FlowShopEnvironment> getAntColony(ProblemConfiguration configurationProvider) {
        return new AntColony<Integer, FlowShopEnvironment>(configurationProvider.getNumberOfAnts()) {
            @Override
            protected Ant<Integer, FlowShopEnvironment> createAnt(FlowShopEnvironment environment) {
                return new AntForFlowShop();
            }
        };
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

        java.awt.EventQueue.invokeLater(() -> {
            SchedulingFrame frame = new SchedulingFrame();
            frame.setSolutionMakespan(problemSolver.getBestSolutionCost());

            frame.setProblemGraph(graph);
            frame.setSolution(problemSolver.getBestSolution().toArray(
                    new Integer[0]));
            frame.setVisible(true);

        });
    }


    /**
     * Reads a scheduling instance in the format used in Taillard's benchmarks
     * (http://mistic.heig-vd.ch/taillard/problemes.dir/ordonnancement.dir/ordonnancement.html)
     *
     * @param fileName         File name
     * @param numberOfJobs     Jobs to consider.
     * @param numberOfMachines Machines available.
     * @return Matrix representation of the problem.
     */
    public static double[][] getTaillardProblemFromFile(String fileName, int numberOfJobs,
                                                        int numberOfMachines) throws IOException {

        double[][] problemRepresentation = new double[numberOfJobs][numberOfMachines];
        int currentMachine = 0;

        File file = new File(Objects.requireNonNull(AcoFlowShopWithIsula.class.getClassLoader().getResource(fileName)).getFile());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
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

}