package org.chapellec.doyouwar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe Utilitaire
 * 
 * @author Cédric Chapelle
 */
public class Utils {
	private static final Logger logger = LogManager.getLogger(Utils.class);

	private Utils() {
		throw new UnsupportedOperationException("Static class, no instanciation allowed.");
	}

	/**
	 * Verifier si le site web est atteignable (permet de tester que la
	 * connexion internet est active)
	 * 
	 * @return
	 */
	public static boolean isWebsiteAvailable() {
		return testURL(ProjectParams.getProperty(ProjectParams.WEB_URL_KEY));
	}

	/**
	 * Tester la réponse d'une URL
	 * 
	 * @param strURL
	 * @return
	 */
	public static boolean testURL(String strURL) {

		boolean connected = false;
		try {
			URL url = new URL(strURL);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.connect();
			connected = true;
			// seules les reponseshttp correspondant a une erreur du client
			// (4XX)
			// ou une erreur du serveur (5XX)
			// sont considerees comme non satisfaisantes
			if (urlConn.getResponseCode() >= 400) {
				logger.error(new StringBuilder("Connected to ").append(strURL)
						.append(" but obtained an http reponse error [")
						.append(+urlConn.getResponseCode()).append("]:")
						.append(urlConn.getResponseMessage()).toString());
				connected = false;
			}

		} catch (IOException e) {
			logger.info("Failed to conect to " + strURL);
			connected = false;
		}
		return connected;
	}

	/**
	 * sauvegarde la reponse en attente en local
	 */
	public static boolean reinitStatsLocally() {
		if (logger.isDebugEnabled()) {
			logger.debug("start deleteStatsLocally()");
		}

		// ecriture dans le fichier
		BufferedWriter writer = null;
		String localStatsFilename = ProjectParams.getProperty(ProjectParams.LOCALDATA_FILE_KEY);
		try {
			writer = new BufferedWriter(new FileWriter(localStatsFilename));
			writer.write("0");
			writer.newLine();
			writer.write("0");
			writer.newLine();
			writer.write("0");
			writer.newLine();
			writer.write("0");
			writer.newLine();
			return true;

		} catch (IOException e) {
			logger.error("Fail to reinitialize local stats in file " + localStatsFilename, e);
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

}
