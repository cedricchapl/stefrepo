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
	 * cl� de message du fichier de donnees serveur
	 */
	public static String SERVERDATA_FILE_KEY = "filename.serverdata";

	/**
	 * cl� de message du fichier de donnees local
	 */
	public static String LOCALDATA_FILE_KEY = "filename.localdata";

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
	 *            nom de la propri�t�
	 * @return valeur de la propri�t�
	 */
	public static String getProperty(String name) {
		if (!loaded) {
			init();
		}
		return props.getProperty(name);

	}

}
