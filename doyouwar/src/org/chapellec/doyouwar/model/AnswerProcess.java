package org.chapellec.doyouwar.model;

/**
 * Gestion statistique des reponses
 * 
 * @author Cédric Chapelle
 *
 */
public interface AnswerProcess {

	/**
	 * 
	 * @return les statistiques
	 */
	Stats getStats();

	/**
	 * Charge les reponses precedentes
	 */
	void loadAnswers();

	/**
	 * Ajoute de la reponse
	 * 
	 * @param answer
	 * @param valYES
	 * @param valNO
	 */
	void addAnswer(String answer, String valYES, String valNO);

	/**
	 * Enregistre la mise a jour des reponses
	 * 
	 * @return true si MAJ ok
	 */
	boolean saveAnswers();

	/**
	 * Lance le traitement complet d'une reponse
	 * 
	 * @param answer
	 * @param valYES
	 * @param valNO
	 * @return les stats mises a jour
	 */
	Stats process(String answer, String valYES, String valNO);

}
