package org.chapellec.doyouwar;

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
		return testURL(ProjectParams.getProperty("web.url"));
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
				logger.error(new StringBuilder("Connexion a ").append(strURL)
						.append(" mais reponse http en erreur [")
						.append(+urlConn.getResponseCode()).append("]:")
						.append(urlConn.getResponseMessage()).toString());
				connected = false;
			}

		} catch (IOException e) {
			logger.info("Echec de la connexion a " + strURL);
			connected = false;
		}
		return connected;
	}
}
