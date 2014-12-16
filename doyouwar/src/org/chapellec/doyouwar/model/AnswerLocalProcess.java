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
 * Gestion statistique des reponses en local (mode deconnecte)
 * 
 * @author Cédric Chapelle
 *
 */
public class AnswerLocalProcess extends AbstractAnswerProcess implements AnswerProcess {

	/**
	 * 
	 */
	public AnswerLocalProcess() {
		super();
		String filename = ProjectParams.getProperty(ProjectParams.LOCALDATA_FILE_KEY);
		statsFile = new File(filename);
		stats = new Stats();
	}

	/**
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
			String pendingStatsYES = reader.readLine();
			String pendingStatsNO = reader.readLine();

			if (StringUtils.isNotBlank(previousStatsYES) && StringUtils.isNotBlank(previousStatsNO)) {
				// si le fichier n'est pas vide
				// alors il doit comporter deja des stats,
				// on tente de les recuperer
				// 1ere ligne du fichier : nombre de YES du serveur
				stats.setNbYES(Integer.valueOf(previousStatsYES));
				// 2eme ligne du fichier : nombre de NO du serveur
				stats.setNbNO(Integer.valueOf(previousStatsNO));
				// 3eme ligne du fichier : nombre de YES en attente en local
				stats.setPendingNbYES(Integer.valueOf(pendingStatsYES));
				// 4eme ligne du fichier : nombre de NO en attente en local
				stats.setPendingNbNO(Integer.valueOf(pendingStatsNO));

				computeStats();
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
			stats.setPendingNbYES(stats.getPendingNbYES() + 1);
		} else if (answer.equals(valNO)) {
			stats.setPendingNbNO(stats.getPendingNbNO() + 1);
		}

	}

	/**
	 * 
	 * @see org.chapellec.doyouwar.model.AnswerProcess#saveAnswers()
	 */
	@Override
	public boolean saveAnswers() {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(statsFile));
			writer.write("" + stats.getNbYES());
			writer.newLine();
			writer.write("" + stats.getNbNO());
			writer.newLine();
			writer.write("" + stats.getPendingNbYES());
			writer.newLine();
			writer.write("" + stats.getPendingNbNO());
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
}
