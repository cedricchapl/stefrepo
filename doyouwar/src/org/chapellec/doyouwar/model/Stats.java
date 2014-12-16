package org.chapellec.doyouwar.model;

/**
 * Statistiques
 * 
 * @author Cédric Chapelle
 *
 */
public class Stats {
	int nbYES = 0;
	int nbNO = 0;
	int pendingNbYES = 0; // local only
	int pendingNbNO = 0; // local only
	int total; // calculated
	int pctYES; // calculated
	int pctNO; // calculated

	/**
	 * @return the nbYES
	 */
	public int getNbYES() {
		return nbYES;
	}

	/**
	 * @param nbYES
	 *            the nbYES to set
	 */
	public void setNbYES(int nbYES) {
		this.nbYES = nbYES;
	}

	/**
	 * @return the nbNO
	 */
	public int getNbNO() {
		return nbNO;
	}

	/**
	 * @param nbNO
	 *            the nbNO to set
	 */
	public void setNbNO(int nbNO) {
		this.nbNO = nbNO;
	}

	/**
	 * @return the pendingNbYES
	 */
	public int getPendingNbYES() {
		return pendingNbYES;
	}

	/**
	 * @param pendingNbYES
	 *            the pendingNbYES to set
	 */
	public void setPendingNbYES(int pendingNbYES) {
		this.pendingNbYES = pendingNbYES;
	}

	/**
	 * @return the pendingNbNO
	 */
	public int getPendingNbNO() {
		return pendingNbNO;
	}

	/**
	 * @param pendingNbNO
	 *            the pendingNbNO to set
	 */
	public void setPendingNbNO(int pendingNbNO) {
		this.pendingNbNO = pendingNbNO;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the pctYES
	 */
	public int getPctYES() {
		return pctYES;
	}

	/**
	 * @param pctYES
	 *            the pctYES to set
	 */
	public void setPctYES(int pctYES) {
		this.pctYES = pctYES;
	}

	/**
	 * @return the pctNO
	 */
	public int getPctNO() {
		return pctNO;
	}

	/**
	 * @param pctNO
	 *            the pctNO to set
	 */
	public void setPctNO(int pctNO) {
		this.pctNO = pctNO;
	}

	/**
	 * Ajoute une ou plusieurs r�ponses YES
	 * 
	 * @param additionalNbYES
	 */
	public void addNbYES(int additionalNbYES) {
		this.nbYES += additionalNbYES;
	}

	/**
	 * Ajoute une ou plusieurs r�ponses NO
	 * 
	 * @param additionalNbNO
	 */
	public void addNbNO(int additionalNbNO) {
		this.nbNO += additionalNbNO;
	}

}
