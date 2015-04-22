package org.chapellec.doyouwar.model;

/**
 * Statistiques
 * 
 * @author Cédric Chapelle
 *
 */
public class Stats {
	
	private Answers lastServerRetrieval = new Answers();

	private int pendingYes = 0; // local only
	private int pendingNo = 0; // local only
	private int total; // calculated
	private int pctYES; // calculated
	private int pctNO; // calculated

	/**
	 * @param lastServerRetrieval
	 *            the lastServerRetrieval to set
	 */
	public void setLastServerRetrieval(Answers lastServerRetrieval) {
		this.lastServerRetrieval = lastServerRetrieval;
	}

	/**
	 * @return the lastServerRetrieval
	 */
	public Answers getLastServerRetrieval() {
		return lastServerRetrieval;
	}

	/**
	 * @return the pendingYes
	 */
	public int getPendingYes() {
		return pendingYes;
	}

	/**
	 * @param pendingYes
	 *            the pendingYes to set
	 */
	public void setPendingYes(int pendingYes) {
		this.pendingYes = pendingYes;
	}

	/**
	 * @return the pendingNo
	 */
	public int getPendingNo() {
		return pendingNo;
	}

	/**
	 * @param pendingNo
	 *            the pendingNo to set
	 */
	public void setPendingNo(int pendingNo) {
		this.pendingNo = pendingNo;
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
	 * Ajoute une ou plusieurs reponses YES
	 * 
	 * @param addedYes
	 */
	public void addYES(int addedYes) {
		lastServerRetrieval.setYes(lastServerRetrieval.getYes() + addedYes);
	}

	/**
	 * Ajoute une ou plusieurs reponses NO
	 * 
	 * @param addedNo
	 */
	public void addNo(int addedNo) {
		lastServerRetrieval.setNo(lastServerRetrieval.getNo() + addedNo);
	}

}
