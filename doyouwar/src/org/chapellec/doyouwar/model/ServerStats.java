/**
 * 
 */
package org.chapellec.doyouwar.model;

/**
 * @author Cédric Chapelle
 *
 */
public class ServerStats {

	int yes;

	int no;

	/**
	 * 
	 */
	public ServerStats() {
	}

	/**
	 * @param no
	 *            the no to set
	 */
	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * @param yes
	 *            the yes to set
	 */
	public void setYes(int yes) {
		this.yes = yes;
	}

	/**
	 * @return the yes
	 */
	public int getYes() {
		return yes;
	}

	/**
	 * @return the no
	 */
	public int getNo() {
		return no;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerStats = yes:" + yes + ", no=" + no;
	}
}
