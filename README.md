# aco-flowshop
A Java Program to solve the Flow-Shop Scheduling Problem using Ant Colony Algorithms. The implemented method is based on the paper "An ant approach to the flow shop problem" by Thomas Stutzle.

The Ant-Colony Algorithm
------------------------
The Ant Colony algorithm used to solve the problem is based on Max-Min Ant System. To implement it, we used the Isula Framework:

```java
      FlowShopProblemSolver problemSolver;

      ProblemConfiguration configurationProvider = new ProblemConfiguration();
      problemSolver = new FlowShopProblemSolver(graph, configurationProvider);
      configurationProvider.setEnvironment(problemSolver.getEnvironment());

      problemSolver.addDaemonActions(
          new StartPheromoneMatrixForMaxMin<Integer>(),
          new FlowShopUpdatePheromoneMatrix());
      problemSolver.getAntColony().addAntPolicies(
          new PseudoRandomNodeSelection<Integer>(), new ApplyLocalSearch());

      problemSolver.solveProblem();
```

The implemented process has the following characteristics:
* We have specialized Ants that build possible solutions while traversing the problem graph. The quality of each solution is expressed by its makespan.
* After an Ant has built a solution, a Local Search Procedure is used to improve its quality. This is accomplished through a Daemon Action.
* The procedure used by an Ant to add another component to its solution is the Pseudo-Random rule proposed in the Ant Colony System algorithm. This algorithm used the policy implementation provided by the framework.
* As we're working with the Max-Min Ant System algorithm, we need to follow its rules regarding pheromone update. For that, we use the Pheromone Initialization Policy available on the Framework, and extend the Update Policy to suit the needs of our problem.

The results
-----------
This program generates a graphical representation of the best solution found:

![Flow Shop Scheduling Solution](http://cptanalatriste.github.io/isula/img/flowshop-solution.PNG)

How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Maven Java Project. You should be able to import it as an existing project to your current workspace.

**This project depends on the Isula Framework**.  You need to download first the Isula Framework Project available on this Github Repository: https://github.com/cptanalatriste/isula

Keep in mind that several file and folder locations were configured on the `ProblemConfiguration.java` file. You need to set values according to your environment in order to avoid a `FileNotFoundException`. 

More about Isula
----------------
Visit the Isula Framework site: http://cptanalatriste.github.io/isula/

Review the Isula JavaDoc: http://cptanalatriste.github.io/isula/doc/

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
