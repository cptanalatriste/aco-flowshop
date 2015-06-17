package pe.edu.pucp.ia.aco.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Class that shows graphically a solution for an instance of the Flow-Shop
 * Schedullig problem.
 * 
 * @author Adrián Pareja (adrian@pareja.com)
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 */
public class SchedulingFrame extends javax.swing.JFrame {

	private static final String MAKESPAN_LABEL = "Solution makespan: ";
	private static final String FRAME_TITLE = "A solution for the Flow-Shop problem";
	private static final long serialVersionUID = 6254510777986769141L;
	private int[] solution;
	private double solutionMakespan;
	private double[][] problemGraph;

	/**
	 * Creates new form SchedulingFrame
	 */
	public SchedulingFrame() {
		initComponents();
	}

	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(FRAME_TITLE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 1330,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 539,
				Short.MAX_VALUE));

		pack();
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		double solutionAux = solutionMakespan;
		
		int red = 0;
		int green = 0;
		int blue = 0;

		int factor = 1;
		while (solutionAux > this.getWidth()) {
			factor = factor * 2;
			solutionAux = solutionAux / 2;
		}

		int posT = 20;
		int posX = 20;
		int posY = 20;

		Color color = null;

		int machines = problemGraph[0].length;
		double[] machinesTime = new double[machines];
		double tiempo = 0;

		int height = this.getHeight() / (machines * 2);

		g.drawString(MAKESPAN_LABEL + solutionMakespan, 350, 380);

		for (int job : solution) {
			Random r = new Random();
			red = r.nextInt(255) + 1;
			green = r.nextInt(255) + 1;
			blue = r.nextInt(255) + 1;
			color = new Color(red, green, blue);
			g.setColor(color);
			g.fillRect(posT, 400, 40, 20);
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf(job), posT + 15, 400);
			g.setColor(color);
			posT = posT + 60;
			posY = 20;
			for (int i = 0; i < machines; i++) {
				tiempo = problemGraph[job][i];
				posY = posY + height;
				if (i == 0) {
					if (posX == 20) {
						machinesTime[i] = machinesTime[i] + tiempo;
						g.fillRect(i * (int) machinesTime[i] / factor, posY,
								(int) tiempo / factor, height);
						posX = 40;
					} else {
						machinesTime[i] = machinesTime[i] + tiempo;
						g.fillRect((int) (machinesTime[i] - tiempo) / factor,
								posY, (int) tiempo / factor, height);
					}
				} else {
					if (machinesTime[i] > machinesTime[i - 1]) {
						g.fillRect((int) (machinesTime[i]) / factor, posY,
								(int) tiempo / factor, height);
						machinesTime[i] = machinesTime[i] + tiempo;
					} else {
						machinesTime[i] = machinesTime[i - 1] + tiempo;
						g.fillRect((int) machinesTime[i - 1] / factor, posY,
								(int) tiempo / factor, height);
					}
				}
			}
		}

	}

	public void setSolution(int[] schedulingJobs) {
		this.solution = schedulingJobs;
	}

	public void setSolutionMakespan(double makespan) {
		this.solutionMakespan = makespan;
	}

	public void setProblemGraph(double[][] matrixs) {
		this.problemGraph = matrixs;
	}

}
