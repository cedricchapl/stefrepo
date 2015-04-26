package org.chapellec.doyouwar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chapellec.doyouwar.Doyouwar;

/**
 * Contrôleur de l'interface de la home
 * 
 * @author Cédric Chapelle
 *
 */
public class HomeController implements Initializable {

	private static final Logger logger = LogManager.getLogger(HomeController.class);

	@FXML
	private Image logo;

	@FXML
	private ComboBox<Locale> changeLng;

	/**
	 * Référence à l'application courante
	 * 
	 * @see www.guigarage.com/2013/12/datafx-controller-framework-preview/
	 * 
	 */
	// TODO utiliser un fwk de DI pour application et data (
	@SuppressWarnings("javadoc")
	private Doyouwar application;

	/**
	 * @param pApplication
	 *            the application to set
	 */
	public void setMain(Doyouwar pApplication) {
		application = pApplication;
		changeLng.setItems(FXCollections.observableArrayList(Locale.ENGLISH, Locale.FRENCH,
				Locale.ITALIAN));
	}

	private Map<String, Locale> locales;

	/**
	 * @param locales
	 *            the locales to set
	 */
	public void setLocales(Map<String, Locale> locales) {
		this.locales = locales;
	}

	/**
	 * @return the locales
	 */
	public Map<String, Locale> getLocales() {
		return locales;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		changeLng.setValue(Locale.getDefault());
		changeLng.setOnAction((event) -> {
			Locale loc = changeLng.getSelectionModel().getSelectedItem();
			Locale.setDefault(loc);
			application.gotoHome();
		});
	}

	/**
	 * handler for #accessProject action button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void accessProject() throws IOException {
		logger.debug("start accessProject()");
		application.gotoVote();
	}

}
