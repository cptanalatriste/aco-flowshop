package pe.edu.pucp.ia.flowshop.util;

/**
 * Utilities methods for Flow-Shop problem solving.
 * 
 * @author Adrián Pareja (adrian@pareja.com)
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * 
 */
public class FlowShopUtils {

	/**
	 * Gets the makespan for an Schedule.
	 * 
	 * @param schedule
	 *            Schedule to evaluate.
	 * @param jobInfo
	 *            Problem graph.
	 * @return Schedule makespan.
	 */
	public static double getScheduleMakespan(int[] schedule, double[][] jobInfo) {
		int machines = jobInfo[0].length;
		double[] machinesTime = new double[machines];
		double tiempo = 0;

		for (int job : schedule) {
			for (int i = 0; i < machines; i++) {
				tiempo = jobInfo[job][i];
				if (i == 0) {
					machinesTime[i] = machinesTime[i] + tiempo;
				} else {
					if (machinesTime[i] > machinesTime[i - 1]) {
						machinesTime[i] = machinesTime[i] + tiempo;
					} else {
						machinesTime[i] = machinesTime[i - 1] + tiempo;
					}
				}
			}
		}
		return machinesTime[machines - 1];
	}
}
