package isula.aco.flowshop;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplyLocalSearch extends AntPolicy<Integer, FlowShopEnvironment> {

    private static Logger logger = Logger.getLogger(ApplyLocalSearch.class
            .getName());

    public ApplyLocalSearch() {
        super(AntPolicyType.AFTER_SOLUTION_IS_READY);
    }

    @Override
    public boolean applyPolicy(FlowShopEnvironment environment,
                               ConfigurationProvider configuration) {

        logger.log(Level.FINE, "Original Solution > Cost: "
                + getAnt().getSolutionCost(environment) + ", Solution: "
                + getAnt().getSolutionAsString());

        // TODO(cgavidia): This needs a HUGE REFACTOR!

        AntForFlowShop antForFlowShop = (AntForFlowShop) getAnt();
        double makespan = getAnt().getSolutionCost(environment);
        List<Integer> currentSolution = getAnt().getSolution();
        Integer[] localSolutionJobs = new Integer[currentSolution.size()];

        List<Integer> jobsList = new ArrayList<>();

        for (int job : currentSolution) {
            jobsList.add(job);
        }

        List<Integer> localSolution = jobsList;

        int indexI = 0;
        boolean lessMakespan = true;

        while (indexI < (currentSolution.size()) && lessMakespan) {
            int jobI = localSolution.get(indexI);
            localSolution.remove(indexI);
            int indexJ = 0;

            while (indexJ < currentSolution.size() && lessMakespan) {
                localSolution.add(indexJ, jobI);

                Integer[] intermediateSolution = new Integer[currentSolution.size()];
                int anotherIndex = 0;

                for (int sol : localSolution) {
                    intermediateSolution[anotherIndex] = sol;
                    anotherIndex++;
                }

                double newMakespan = antForFlowShop.getScheduleMakespan(
                        Arrays.asList(intermediateSolution), environment.getProblemRepresentation());

                if (newMakespan < makespan) {
                    makespan = newMakespan;
                    lessMakespan = false;
                } else {
                    localSolution.remove(indexJ);
                }

                indexJ++;
            }

            if (lessMakespan) {
                localSolution.add(indexI, jobI);
            }
            indexI++;
        }

        int index = 0;
        for (int job : localSolution) {
            localSolutionJobs[index] = job;
            index++;
        }
        getAnt().setSolution(new ArrayList<>(Arrays.asList(localSolutionJobs)));

        return true;
    }

}
