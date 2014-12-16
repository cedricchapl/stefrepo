package org.chapellec.doyouwar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.chapellec.doyouwar.Utils;
import org.chapellec.doyouwar.model.AnswerLocalProcess;
import org.chapellec.doyouwar.model.AnswerProcess;
import org.chapellec.doyouwar.model.AnswerServerProcess;
import org.chapellec.doyouwar.model.Stats;

/**
 * Contrôleur de l'interface de vote
 * 
 * @author Cédric Chapelle
 *
 */
public class VoteController implements Initializable {

	@FXML
	private VBox votePane;

	@FXML
	private GridPane voteResultsPane;

	@FXML
	private ToggleGroup answerGroup;

	@FXML
	private RadioButton answerYES;

	@FXML
	private RadioButton answerNO;

	@FXML
	private Label resultsYES;

	@FXML
	private Label resultsNO;

	@FXML
	private Label resultsTotal;

	@FXML
	private Label voteInfo;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		voteResultsPane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
		voteResultsPane.setVisible(false);
	}

	/**
	 * handler for submit button
	 */
	@FXML
	public void submitAction() {
		StringBuilder infoBuilder = new StringBuilder();
		RadioButton selRadio = (RadioButton) answerGroup.getSelectedToggle();
		if (selRadio == null) {
			infoBuilder.append("You MUST choose an answer... coward !");
		} else {
			String answer = selRadio.getText();
			String warning = "";
			AnswerProcess process = null;

			boolean serverOK = isWebServiceAvailable();
			// maj de stats
			if (serverOK) {
				process = new AnswerServerProcess();
				process.process(answer, answerYES.getText(), answerNO.getText());
				warning = " (Succeeded to connect to the server, displaying up-to-date statistics...)";
			} else {
				process = new AnswerLocalProcess();
				process.process(answer, answerYES.getText(), answerNO.getText());
				warning = " (Failed to connect to the server, displaying last known statistics...)";
			}

			// formattage des stats pour affichage
			Stats stats = process.getStats();
			resultsYES.setText(stats.getPctYES() + "%");
			resultsNO.setText(stats.getPctNO() + "%");
			resultsTotal.setText(String.format("%05d", stats.getTotal()));
			infoBuilder.append(warning);
			voteResultsPane.setVisible(true);

		}
		voteInfo.setText(infoBuilder.toString());
	}

	/**
	 * Teste si le web service Doyouwantwar est accessible
	 * 
	 * @return
	 */
	private boolean isWebServiceAvailable() {
		return Utils.testURL("http://www.yahoo.fr");
	}

}