package org.chapellec.doyouwar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
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
	private Hyperlink changeLng;
	
	private ResourceBundle bundle;

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

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		bundle = resourceBundle;		
//		changeLng.setText(bundle.getString("changelng.link"));
		
		changeLng.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (application.getCurrentLocale().equals(Locale.UK)) {
					application.setCurrentLocale(Locale.FRANCE);
				} else {
					application.setCurrentLocale(Locale.UK);
				}
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setResources(ResourceBundle.getBundle("bundles.messages",
						application.getCurrentLocale()));
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
