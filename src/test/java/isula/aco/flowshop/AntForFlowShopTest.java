package isula.aco.flowshop;

import isula.aco.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static pe.edu.pucp.ia.aco.AcoFlowShopWithIsula.getTaillardProblemFromFile;
import static pe.edu.pucp.ia.aco.AcoFlowShopWithIsulaTest.*;

public class AntForFlowShopTest {

    private AntForFlowShop ant;
    private FlowShopEnvironment environment;

    @BeforeEach
    void setUp() throws InvalidInputException, IOException {
        double[][] problemRepresentation = getTaillardProblemFromFile(TEST_DATA_SET, NUMBER_OF_JOBS,
                NUMBER_OF_MACHINES);

        ant = new AntForFlowShop();
        environment = new FlowShopEnvironment(problemRepresentation);
    }


    @Test
    void testIsSolutionReady() {

        ant.visitNode(0, environment);
        ant.visitNode(1, environment);
        ant.visitNode(2, environment);
        assertFalse(ant.isSolutionReady(environment));

        ant.visitNode(3, environment);
        assertTrue(ant.isSolutionReady(environment));
    }

    @Test
    public void testGetSolutionCost() {
        ant.visitNode(0, environment);
        ant.visitNode(1, environment);
        ant.visitNode(2, environment);
        ant.visitNode(3, environment);

        double expectedCost = 29.0;
        double currentCost = ant.getSolutionCost(environment, ant.getSolution());

        assertEquals(expectedCost, currentCost, 0.001);

    }


}