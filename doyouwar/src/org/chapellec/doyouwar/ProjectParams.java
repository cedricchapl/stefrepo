package org.chapellec.doyouwar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Paramètres du projet
 * 
 * @author Cédric Chapelle
 *
 */
public class ProjectParams {

	private static boolean loaded = false;

	private static Properties props = new Properties();

	/**
	 * cle de message du fichier de donnees serveur
	 */
	public static String SERVERDATA_FILE_KEY = "filename.serverdata";

	/**
	 * cle de message du fichier de donnees local
	 */
	public static String LOCALDATA_FILE_KEY = "filename.localdata";
	
	/**
	 * cle de message de l'url du service rest de maj/consultation des votes
	 */
	public static String REST_URL_KEY = "rest.url";
	

	private static boolean init() {
		try {
			InputStream is = ProjectParams.class.getClassLoader().getResourceAsStream(
					"project.properties");
			props.load(is);
			loaded = true;
		} catch (IOException e) {
			loaded = false;
		}
		return loaded;
	};

	/**
	 * @param name
	 *            nom de la propriete
	 * @return valeur de la propriete
	 */
	public static String getProperty(String name) {
		if (!loaded) {
			init();
		}
		return props.getProperty(name);

	}

}
