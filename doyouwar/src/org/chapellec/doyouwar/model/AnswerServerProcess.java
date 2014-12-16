package org.chapellec.doyouwar.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.chapellec.doyouwar.ProjectParams;

/**
 * Gestion statistique des reponses au niveau du serveur (mode connecte)
 * 
 * @author Cédric Chapelle
 *
 */
public class AnswerServerProcess extends AbstractAnswerProcess implements AnswerProcess {

	/**
	 * Local process pour synchronisation local / server
	 */
	AnswerLocalProcess localProcess;

	/**
	 * 
	 */
	public AnswerServerProcess() {
		super();
		String filename = ProjectParams.getProperty(ProjectParams.SERVERDATA_FILE_KEY);
		statsFile = new File(filename);
		stats = new Stats();
		localProcess = new AnswerLocalProcess();
	}

	/**
	 * TODO Pour l'instant, le serveur est implemente comme un deuxieme fichier
	 * local. A prevoir, appel webservice de consultation des stats courantes
	 * 
	 * @see org.chapellec.doyouwar.model.AnswerProcess#loadAnswers()
	 */
	@Override
	public void loadAnswers() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(statsFile));
			String previousStatsYES = reader.readLine();
			String previousStatsNO = reader.readLine();

			if (StringUtils.isNotBlank(previousStatsYES) && StringUtils.isNotBlank(previousStatsNO)) {
				// si le fichier n'est pas vide
				// alors il doit comporter deja des stats,
				// on tente de les recuperer
				// 1ere ligne du fichier : nombre de YES du serveur
				stats.setNbYES(Integer.valueOf(previousStatsYES));
				// 2eme ligne du fichier : nombre de NO du serveur
				stats.setNbNO(Integer.valueOf(previousStatsNO));
				// 3eme ligne du fichier : nombre de YES en attente en local
				stats.setPendingNbYES(0);
				// 4eme ligne du fichier : nombre de NO en attente en local
				stats.setPendingNbNO(0);
			}
		} catch (IOException e) {
			System.err.println("Echec de la lecture de " + statsFile.getName());
			System.err.println(e);
		} catch (NumberFormatException e) {
			System.err.println("Echec de la lecture de " + statsFile.getName());
			System.err.println(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * 
	 * @see org.chapellec.doyouwar.model.AnswerProcess#addAnswer(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void addAnswer(String answer, String valYES, String valNO) {
		if (answer.equals(valYES)) {
			stats.setNbYES(stats.getNbYES() + 1);
		} else if (answer.equals(valNO)) {
			stats.setNbNO(stats.getNbNO() + 1);
		}

	}

	/**
	 * TODO Pour les tests le serveur est implemente comme un deuxieme fichier
	 * local. Dans la version finale, appel webservice de mise a jour des
	 * reponses
	 * 
	 * @see org.chapellec.doyouwar.model.AnswerProcess#saveAnswers()
	 */
	@Override
	public boolean saveAnswers() {
		// synchro prealable
		syncLocalAndServerStats();

		// ecriture dans le fichier
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(statsFile));
			writer.write("" + stats.getNbYES());
			writer.newLine();
			writer.write("" + stats.getNbNO());
			writer.newLine();
			return true;
		} catch (IOException e) {
			System.err.println("Echec de l'ecriture de " + statsFile.getName());
			System.err.println(e);
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * Synchronise les statistiques du serveur et de la machine locale
	 */
	private void syncLocalAndServerStats() {
		localProcess.loadAnswers();
		Stats localStats = localProcess.getStats();

		// transfert des reponses en attente
		stats.addNbYES(localStats.getPendingNbYES());
		stats.addNbNO(localStats.getPendingNbNO());

		// sauvegarde des nouvelles stats en local et sur le serveur
		localProcess.setStats(stats);
		localProcess.saveAnswers();
	}

}
