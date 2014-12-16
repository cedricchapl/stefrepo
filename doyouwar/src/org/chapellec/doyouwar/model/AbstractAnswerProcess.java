
package org.chapellec.doyouwar.model;

import java.io.File;

/**
 * @author C�dric Chapelle
 *
 */
public abstract class AbstractAnswerProcess implements AnswerProcess {

	protected File statsFile;

	protected Stats stats;

	/**
	 * 
	 */
	protected AbstractAnswerProcess() {
	}

	/**
	 * @return the stats
	 */
	public Stats getStats() {
		return stats;
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(Stats stats) {
		this.stats = stats;
	}

	/**
	 * Calcule les stats a partir des r�ponses
	 */
	public void computeStats() {
		stats.setTotal(stats.getNbYES() + stats.getNbNO() + stats.getPendingNbYES()
				+ stats.getPendingNbNO());
		stats.setPctYES(Math.round((stats.getNbYES() + stats.getPendingNbYES()) * 100
				/ stats.getTotal()));
		stats.setPctNO(100 - stats.getPctYES());
	}
		
	/**
	 * Process globale de traitement d'une r�ponse
	 * @return
	 */
	public Stats process(String answer, String valYES, String valNO) {
		loadAnswers();
		addAnswer(answer, valYES, valNO);
		saveAnswers();
		computeStats();
		Stats stats = getStats();
		return stats;
	}
}
