package org.chapellec.doyouwar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

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
	private VBox homePane;

	@FXML
	private Image logo;

	@FXML
	private ComboBox<String> changeLng;

	/**
	 * Référence à l'application courante
	 */
	private Doyouwar application;

	/**
	 * @param application
	 *            the application to set
	 */
	public void setMain(Doyouwar application) {
		this.application = application;
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

	/**
	 * 
	 */
	public void initRefData() {
		Map<String, Locale> tempLocales = new LinkedHashMap<String, Locale>();
		tempLocales.put("en", Locale.ENGLISH);
		tempLocales.put("fr", Locale.FRENCH);
		tempLocales.put("it", Locale.ITALIAN);
		setLocales(tempLocales);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		
		initRefData();
		changeLng.getItems().addAll(getLocales().keySet());

		changeLng.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				@SuppressWarnings("unchecked")
				String localeKey = ((ComboBox<String>) event.getSource()).getValue();

				application.setCurrentLocale(getLocales().get(localeKey));

				application.gotoHome();
			}
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
