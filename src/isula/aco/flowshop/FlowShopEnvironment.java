package isula.aco.flowshop;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;
import isula.aco.exception.MethodNotImplementedException;

import java.util.logging.Logger;

public class FlowShopEnvironment extends Environment {

    private static Logger logger = Logger.getLogger(FlowShopEnvironment.class
            .getName());

    private double[][] problemRepresentation;

    private int numberOfJobs;

    /**
     * Environment for the Flow Shop Scheduling Problem.
     *
     * @param problemGraph Graph representation of the problem.
     * @throws InvalidInputException When the graph is incorrectly formed.
     */
    public FlowShopEnvironment(double[][] problemGraph) throws InvalidInputException {
        super();

        if (this.isProblemRepresentationValid(problemGraph)) {
            this.problemRepresentation = problemGraph;
            this.setPheromoneMatrix(createPheromoneMatrix());
        } else {
            throw new InvalidInputException();
        }

        this.numberOfJobs = problemGraph.length;

        logger.info("Number of Jobs: " + numberOfJobs);
    }

    public int getNumberOfJobs() {
        return getProblemRepresentation().length;
    }

    protected boolean isProblemRepresentationValid(double[][] problemRepresentation) {
        int numberOfMachines = problemRepresentation[0].length;
        int jobs = problemRepresentation.length;

        for (int i = 1; i < jobs; i++) {
            if (problemRepresentation[i].length != numberOfMachines) {
                return false;
            }
        }

        logger.info("Number of Machines: " + numberOfMachines);
        return true;
    }

    @Override
    protected double[][] createPheromoneMatrix()
            throws MethodNotImplementedException {
        if (this.getProblemRepresentation() != null) {
            int jobs = getNumberOfJobs();
            return new double[jobs][jobs];
        }

        return null;

    }

    public double[][] getProblemRepresentation() {
        return problemRepresentation;
    }
}
