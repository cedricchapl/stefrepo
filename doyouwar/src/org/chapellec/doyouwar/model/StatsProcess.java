package org.chapellec.doyouwar.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chapellec.doyouwar.ProjectParams;
import org.chapellec.doyouwar.Utils;

/**
 * Traitement des réponses selon le mode online/offline
 * 
 * @author Cédric Chapelle
 *
 */
public class StatsProcess {

	private static final Logger logger = LogManager.getLogger(StatsProcess.class);

	/**
	 * 
	 */
	public StatsProcess() {
		super();
		localStatsFile = new File(ProjectParams.getProperty(ProjectParams.LOCALDATA_FILE_KEY));
		stats = new Stats();
	}

	protected File localStatsFile;

	protected Stats stats;

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
	 * Process global de traitement d'une reponse
	 * 
	 * @return
	 */
	public boolean process(String answer, String valYES, String valNO) {
		if (logger.isDebugEnabled()) {
			logger.debug("start process(" + answer + ")");
		}
		// TODO test load remote a supprimer
		load(true);
		// fin test
		loadLocally();
		if (sendPending()) {
			boolean remoteTry = saveCurrent(true, answer, valYES, valNO);
			load(remoteTry);
			compute();
			return true;
		} else {
			saveCurrentLocally(answer, valYES, valNO);
			compute();
			return false;
		}
	}

	/**
	 * Tente de transférer les réponses en attente depuis le fichier local vers
	 * le serveur
	 * 
	 * @return
	 */
	public boolean sendPending() {
		logger.debug("start sendPending()");
		if (stats != null && (stats.getPendingNbYES() + stats.getPendingNbNO()) > 0) {

			if (!Utils.isWebsiteAvailable()) {
				logger.info("sendPending() : impossible to connect to the internet to synchronize pending local answers to server... next time maybe !");
				return false;
			} else {
				return new AppClient().post(stats.getPendingNbYES(), stats.getPendingNbNO());
			}
		}
		logger.debug("sendPending() : no pending local answers to synchronize.");
		return true;
	}

	/**
	 * sauvegarde la reponse courante
	 * 
	 * @return true pour des stats serveur
	 *         false pour des stats local
	 */
	public boolean saveCurrent(boolean remoteTry, String answer, String valYES, String valNO) {
		if (logger.isDebugEnabled()) {
			logger.debug("start saveCurrent(" + remoteTry + "," + answer + ")");
		}

		if (remoteTry && Utils.isWebsiteAvailable()) {

			// maj des stats courantes
			int yes = 0;
			int no = 0;
			if (answer.equals(valYES)) {
				yes = 1;
			} else {
				no = 1;
			}

			if (new AppClient().post(yes, no))
				return true;
		}

		logger.info("impossible de contacter le serveur pour envoi de la reponse courante ==> stockage en local");
		saveCurrentLocally(answer, valYES, valNO);
		return true;
	}

	/**
	 * sauvegarde la reponse en attente en local
	 */
	public boolean saveCurrentLocally(String answer, String valYES, String valNO) {
		if (logger.isDebugEnabled()) {
			logger.debug("start saveCurrentLocally(" + answer + ")");
		}
		// maj des stats courantes
		if (answer.equals(valYES)) {
			stats.setPendingNbYES(stats.getPendingNbYES() + 1);
		} else {
			stats.setPendingNbNO(stats.getPendingNbNO() + 1);
		}

		// ecriture dans le fichier
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(localStatsFile));
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
			System.err.println("Echec de l'ecriture de " + localStatsFile.getName());
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
	 * Charger les statistiques courantes
	 * 
	 * @return true pour demander si possible des stats serveur
	 *         false pour demander des stats local
	 */
	public boolean load(boolean remoteTry) {
		if (logger.isDebugEnabled()) {
			logger.debug("start load(" + remoteTry + ")");
		}

		if (remoteTry && Utils.isWebsiteAvailable()) {
			ServerStats remoteStats = new AppClient().get();
			if (remoteStats != null) {
				stats.setNbYES(remoteStats.getYes());
				stats.setNbNO(remoteStats.getNo());
				return true;
			}
		}
		loadLocally();
		return false;
	}

	/**
	 * 
	 */
	public void loadLocally() {
		if (logger.isDebugEnabled()) {
			logger.debug("start loadLocally()");
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(localStatsFile));
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
			}
		} catch (IOException e) {
			logger.error("Echec de la lecture de " + localStatsFile.getName());
			logger.error(e);
		} catch (NumberFormatException e) {
			logger.error("Echec de la lecture de " + localStatsFile.getName());
			logger.error(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Calcule les stats a partir des reponses
	 */
	public void compute() {
		logger.debug("start compute()");

		stats.setTotal(stats.getNbYES() + stats.getNbNO() + stats.getPendingNbYES()
				+ stats.getPendingNbNO());
		stats.setPctYES(Math.round((stats.getNbYES() + stats.getPendingNbYES()) * 100
				/ stats.getTotal()));
		stats.setPctNO(100 - stats.getPctYES());
	}

}
