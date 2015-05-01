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
		loadLocally();
		if (ProjectParams.isProductionMode() && sendPending()) {
			boolean remote = true;
			remote = saveCurrent(remote, answer, valYES, valNO);
			remote = load(remote);
			updateLocalFile();
			compute();
			return remote;
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
		if (stats.getPendingYes() + stats.getPendingNo() > 0) {

			if (!Utils.isWebsiteAvailable()) {
				logger.info("sendPending() : unavailable connection to the internet for synchronizing pending local answers to server... next time maybe !");
				return false;
			} else {
				if (new AppClient().post(new Answers(stats.getPendingYes(), stats.getPendingNo()))) {
					stats.setPendingYes(0);
					stats.setPendingNo(0);
					return true;
				}
				logger.error("sendPending() : error when synchronizing pending local answers.");
				return false;
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
			Answers data = new Answers();
			if (answer.equals(valYES))
				data.setYes(1);
			if (answer.equals(valNO))
				data.setNo(1);

			if (new AppClient().post(data)) {
				return true;
			}

		}

		logger.info("failed to contact server for sending current answer ==> saving locally...");
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
			stats.setPendingYes(stats.getPendingYes() + 1);
		} else {
			stats.setPendingNo(stats.getPendingNo() + 1);
		}

		// ecriture dans le fichier
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(localStatsFile));
			writer.write("" + stats.getLastServerRetrieval().getYes());
			writer.newLine();
			writer.write("" + stats.getLastServerRetrieval().getNo());
			writer.newLine();
			writer.write("" + stats.getPendingYes());
			writer.newLine();
			writer.write("" + stats.getPendingNo());
			writer.newLine();
			return true;

		} catch (IOException e) {
			logger.error("Fail to write in file " + localStatsFile.getName(), e);
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * sauvegarde la reponse en attente en local
	 */
	public boolean updateLocalFile() {
		if (logger.isDebugEnabled()) {
			logger.debug("start updateLocalStatsFile()");
		}

		// ecriture dans le fichier
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(localStatsFile));
			writer.write("" + stats.getLastServerRetrieval().getYes());
			writer.newLine();
			writer.write("" + stats.getLastServerRetrieval().getNo());
			writer.newLine();
			writer.write("" + stats.getPendingYes());
			writer.newLine();
			writer.write("" + stats.getPendingNo());
			writer.newLine();
			return true;

		} catch (IOException e) {
			logger.error("Fail to write in file " + localStatsFile.getName(), e);
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e);
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
			Answers remoteAnswers = new AppClient().get();
			if (remoteAnswers != null) {
				stats.setLastServerRetrieval(remoteAnswers);
				return true;
			}
		}
		loadLocally();
		return false;
	}

	/**
	 * 
	 */
	public boolean loadLocally() {
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
				// 1ere et 2e lignes du fichier : derniers nombres de YES et NO
				// connus du serveur
				stats.setLastServerRetrieval(new Answers(Integer.valueOf(previousStatsYES), Integer
						.valueOf(previousStatsNO)));
				// 3eme ligne du fichier : nombre de YES en attente en local
				stats.setPendingYes(Integer.valueOf(pendingStatsYES));
				// 4eme ligne du fichier : nombre de NO en attente en local
				stats.setPendingNo(Integer.valueOf(pendingStatsNO));
			}
			return true;
		} catch (IOException e) {
			logger.error("Failed to read file " + localStatsFile.getName(), e);
			return false;
		} catch (NumberFormatException e) {
			logger.error("Failed to read file " + localStatsFile.getName(), e);
			return false;
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

		stats.setTotal(stats.getLastServerRetrieval().getYes()
				+ stats.getLastServerRetrieval().getNo() + stats.getPendingYes()
				+ stats.getPendingNo());
		stats.setPctYES(Math.round((stats.getLastServerRetrieval().getYes() + stats.getPendingYes())
				* 100 / stats.getTotal()));
		stats.setPctNO(100 - stats.getPctYES());
	}
}
