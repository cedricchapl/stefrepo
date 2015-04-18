package org.chapellec.doyouwar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	
	/**
	 * Référence à l'application courante
	 */
	private Doyouwar application;

	/**
	 * @param application the application to set
	 */
	public void setMain(Doyouwar application) {
		this.application = application;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
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
