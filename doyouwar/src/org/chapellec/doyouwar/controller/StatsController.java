package org.chapellec.doyouwar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chapellec.doyouwar.Doyouwar;
import org.chapellec.doyouwar.model.Stats;

/**
 * Contrôleur de la page de statistiques
 * 
 * @author Cédric Chapelle
 *
 */
public class StatsController implements Initializable {

	private static final Logger logger = LogManager.getLogger(StatsController.class);

	/**
	 * Référence à l'application courante
	 */
	private Doyouwar application;

	@FXML
	private Label resultsYES;

	@FXML
	private Label resultsNO;

	@FXML
	private Label resultsTotal;

	@FXML
	private Label voteInfo;

	@FXML
	private GridPane resultsPane;

	/**
	 * @param application
	 *            the application to set
	 */
	public void setMain(Doyouwar application) {
		this.application = application;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		resultsPane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
	}

	/**
	 * Formatte et affiche les stats
	 * 
	 * @param stats
	 * @param warning
	 */
	public void displayData(Stats stats, String warning) {
		logger.debug("start displayData()");		
		// formattage des stats pour affichage
		resultsYES.setText(stats.getPctYES() + "%");
		resultsNO.setText(stats.getPctNO() + "%");
		resultsTotal.setText(String.format("%05d", stats.getTotal()));
		voteInfo.setText(warning);
		// affichage des stats pendant une duree limitee
		resultsPane.setVisible(true);				
		FadeTransition ft = new FadeTransition(Duration.seconds(5), resultsPane);
		ft.setDelay(Duration.seconds(5));
		ft.setFromValue(1.0);
		ft.setToValue(0.1);
		ft.setCycleCount(1);
		ft.setAutoReverse(false);
		ft.play();
		ft.setOnFinished(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				logger.debug("end displayData()");
				backHomeAction();				
			}
		});
	}

	/**
	 * Retourne a la home
	 */
	@FXML
	public void backHomeAction() {
		logger.debug("start backHomeAction()");		
		application.gotoHome();
	}
}
