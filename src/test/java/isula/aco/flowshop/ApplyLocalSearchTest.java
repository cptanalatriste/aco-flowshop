package isula.aco.flowshop;

import isula.aco.ConfigurationProvider;
import isula.aco.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import pe.edu.pucp.ia.aco.config.ProblemConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pe.edu.pucp.ia.aco.AcoFlowShopWithIsula.getTaillardProblemFromFile;
import static pe.edu.pucp.ia.aco.AcoFlowShopWithIsulaTest.*;

class ApplyLocalSearchTest {

    @Test
    void testApplyPolicy() throws IOException, InvalidInputException {
        double[][] problemRepresentation = getTaillardProblemFromFile(TEST_DATA_SET, NUMBER_OF_JOBS,
                NUMBER_OF_MACHINES);
        FlowShopEnvironment environment = new FlowShopEnvironment(problemRepresentation);
        ConfigurationProvider configurationProvider = new ProblemConfiguration(problemRepresentation);
        ApplyLocalSearch antPolicy = new ApplyLocalSearch();

        AntForFlowShop ant = new AntForFlowShop();
        antPolicy.setAnt(ant);

        ant.visitNode(0, environment);
        ant.visitNode(1, environment);
        ant.visitNode(2, environment);
        ant.visitNode(3, environment);

        List<Integer> solutionBeforeImprovement = new ArrayList<>(ant.getSolution());
        double costBeforeImprovement = AntForFlowShop.getScheduleMakespan(solutionBeforeImprovement,
                environment.getProblemRepresentation());

        antPolicy.applyPolicy(environment, configurationProvider);

        List<Integer> solutionAfterImprovement = new ArrayList<>(ant.getSolution());
        double costAfterImprovement = AntForFlowShop.getScheduleMakespan(solutionAfterImprovement,
                environment.getProblemRepresentation());

        assertTrue(costAfterImprovement < costBeforeImprovement);

    }
}