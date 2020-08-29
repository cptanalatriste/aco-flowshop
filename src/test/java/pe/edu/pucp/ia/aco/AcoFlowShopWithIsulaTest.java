package pe.edu.pucp.ia.aco;

import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static pe.edu.pucp.ia.aco.AcoFlowShopWithIsula.getTaillardProblemFromFile;

public class AcoFlowShopWithIsulaTest {

    public static final String TEST_DATA_SET = "sampleTaillardFile.data";
    public static final Integer NUMBER_OF_JOBS = 4;
    public static final Integer NUMBER_OF_MACHINES = 3;

    @Test
    public void testReadingTaillardProblemFromFile() throws IOException {

        double[][] problemRepresentation = getTaillardProblemFromFile(TEST_DATA_SET, NUMBER_OF_JOBS,
                NUMBER_OF_MACHINES);
        double[][] expectedProblemRepresentation = {{4, 2, 1}, {3, 6, 2}, {7, 2, 3}, {1, 5, 8}};

        assertArrayEquals(expectedProblemRepresentation, problemRepresentation);

    }
}