package org.chapellec.doyouwar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe Utilitaire
 * 
 * @author Cédric Chapelle
 */
public class Utils {

	private Utils() {
		throw new UnsupportedOperationException("Static class, no instanciation allowed.");
	}

	/**
	 * Teste la r�ponse d'une URL
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
				System.err.println("Connexion a " + strURL + " mais reponse http en erreur ["
						+ urlConn.getResponseCode() + "]:" + urlConn.getResponseMessage());
				connected = false;
			}

		} catch (IOException e) {
			System.err.println("Echec de la connexion a " + strURL);
			connected = false;
		}
		return connected;
	}
}
