package org.chapellec.doyouwar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chapellec.doyouwar.Doyouwar;
import org.chapellec.doyouwar.model.Stats;
import org.chapellec.doyouwar.model.StatsProcess;

/**
 * Contrôleur de l'interface de vote
 * 
 * @author Cédric Chapelle
 *
 */
public class VoteController implements Initializable {

	private static final Logger logger = LogManager.getLogger(VoteController.class);

	/**
	 * Référence à l'application courante
	 */
	private Doyouwar application;

	ResourceBundle bundle;

	/**
	 * @param application
	 *            the application to set
	 */
	public void setMain(Doyouwar application) {
		this.application = application;
	}

	@FXML
	private VBox votePane;

	@FXML
	private ToggleGroup answerGroup;

	@FXML
	private RadioButton answerYES;

	@FXML
	private RadioButton answerNO;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		bundle = resourceBundle;
	}

	/**
	 * handler for submit button
	 */
	@FXML
	public void submitVote() {
		logger.debug("start submitVote()");
		RadioButton selRadio = (RadioButton) answerGroup.getSelectedToggle();
		if (selRadio != null) {
			String answer = selRadio.getText();

			StatsProcess process = new StatsProcess();

			String warning = "";
			if (process.process(answer, answerYES.getText(), answerNO.getText())) {
				warning = bundle.getString("stats_connect_on_msg");
			} else {
				warning = bundle.getString("stats_connect_off_msg");
			}

			Stats stats = process.getStats();

			displayStats(stats, warning);
		}
	}

	/**
	 * 
	 * @param stats
	 * @param warning
	 */
	public void displayStats(Stats stats, String warning) {
		logger.debug("start displayStats()");
		application.gotoStats(stats, warning);
	}

}