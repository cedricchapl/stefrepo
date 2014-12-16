package org.chapellec.doyouwar.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Contrôleur de l'interface de la home
 * 
 * @author Cédric Chapelle
 *
 */
public class HomeController extends AbstractPageController {

	@FXML
	private VBox homePane;

	/**
	 * handler for continue button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void continueAction() throws IOException {
		System.out.println("continueAction");
		selectedView.set(getClass().getResource("../view/Vote.fxml"));
	}

}
