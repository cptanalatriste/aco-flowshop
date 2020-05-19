# aco-flowshop
A Java Program to solve the Flow-Shop Scheduling Problem using Ant Colony Algorithms. The implemented method is based on the paper ["An ant approach to the flow shop problem" by Thomas Stutzle](https://www.researchgate.net/publication/2593620_An_Ant_Approach_to_the_Flow_Shop_Problem).

The Ant-Colony Algorithm
------------------------
The Ant Colony algorithm used to solve the problem is based on Max-Min Ant System. To implement it, we used the Isula Framework:

```java
      ProblemConfiguration configurationProvider = new ProblemConfiguration(problemRepresentation);
      AntColony<Integer, FlowShopEnvironment> colony = getAntColony(configurationProvider);
      FlowShopEnvironment environment = new FlowShopEnvironment(problemRepresentation);
      configurationProvider.setEnvironment(environment);

      AcoProblemSolver<Integer, FlowShopEnvironment> solver = new AcoProblemSolver<>();
      solver.initialize(environment, colony, configurationProvider);

      solver.addDaemonActions(
              new StartPheromoneMatrix<Integer, FlowShopEnvironment>(),
              new FlowShopUpdatePheromoneMatrix());
      solver.getAntColony().addAntPolicies(
              new PseudoRandomNodeSelection<Integer, FlowShopEnvironment>(),
              new ApplyLocalSearch());

      solver.solveProblem();
```

The implemented process has the following characteristics:
* We have specialized Ants that build possible solutions while traversing the problem graph. The quality of each solution is expressed by its makespan.
* After an Ant has built a solution, a Local Search Procedure is used to improve its quality. This is accomplished through the Daemon Action `ApplyLocalSearch`.
* The procedure used by an Ant to add another component to its solution is the Pseudo-Random rule proposed in the Ant Colony System algorithm. This algorithm used the policy implementation provided by the framework in the class `PseudoRandomNodeSelection`.
* As we're working with the Max-Min Ant System algorithm, we need to follow its rules regarding pheromone update. For that, we use the Pheromone Initialization Policy available on the Framework on `StartPheromoneMatrix`, and extend the Update Policy to suit the needs of our problem.

The results
-----------
This program generates a graphical representation of the best solution found:

![Flow Shop Scheduling Solution](http://cptanalatriste.github.io/isula/img/flowshop-solution.PNG)

How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Maven Java Project. As such, it is strongly recommended that you have Maven installed before working with it.

**This project depends on the Isula Framework**.  You need to download and install the Isula Framework Project on your local Maven repository. Follow the instructions available in https://github.com/cptanalatriste/isula

Keep in mind that several file and folder locations were configured on the `ProblemConfiguration.java` file. You need to set values according to your environment in order to avoid a `FileNotFoundException`. Once this is ready, you can launch this project by executing `mvn exec:java -Dexec.mainClass="pe.edu.pucp.ia.aco.AcoFlowShopWithIsula"` from the project root folder.

More about Isula
----------------
Visit the Isula Framework site: http://cptanalatriste.github.io/isula/

Review the Isula JavaDoc: http://cptanalatriste.github.io/isula/doc/

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
