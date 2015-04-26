package org.chapellec.doyouwar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chapellec.doyouwar.controller.HomeController;
import org.chapellec.doyouwar.controller.StatsController;
import org.chapellec.doyouwar.controller.VoteController;
import org.chapellec.doyouwar.model.Stats;

/**
 * Lanceur de l'application
 * 
 * @author Cédric Chapelle
 *
 */
public class Doyouwar extends Application {

	private static final Logger logger = LogManager.getLogger(Doyouwar.class);

	/**
	 * Mode de l'application en production
	 */
	public static final String APP_MODE_PROD = "prod";

	private Stage stage;

	@Override
	public void start(Stage primaryStage) {
		logger.debug("start start()");

		stage = primaryStage;
		stage.setTitle("DOYOUWAR");
		gotoHome();
		stage.show();
	}

	private Initializable replaceSceneContent(String fxml) throws Exception {
		logger.debug("start replaceSceneContent()");

		FXMLLoader loader = new FXMLLoader();
		InputStream in = Doyouwar.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Doyouwar.class.getResource(fxml));
		loader.setResources(ResourceBundle.getBundle("bundles.messages", Locale.getDefault()));
		Parent page;
		try {
			page = (Parent) loader.load(in);
		} finally {
			in.close();
		}
		Scene scene = new Scene(page, 800, 600);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
		
		return (Initializable) loader.getController();
	}

	/**
	 * Charge la page Home
	 */
	public void gotoHome() {
		logger.debug("start gotoHome()");
		try {
			HomeController homeCtrl = (HomeController) replaceSceneContent("./view/Home.fxml");
			homeCtrl.setMain(this);
		} catch (Exception ex) {
			logger.fatal(ex);
			Platform.exit();
		}
	}

	/**
	 * Charge la page Vote
	 */
	public void gotoVote() {
		logger.debug("start gotoVote()");
		try {
			VoteController voteCtrl = (VoteController) replaceSceneContent("./view/Vote.fxml");
			voteCtrl.setMain(this);
		} catch (Exception ex) {
			logger.fatal(ex);
			Platform.exit();
		}
	}

	/**
	 * Charge la page statistiques
	 * 
	 * @param stats
	 * @param warning
	 */
	public void gotoStats(Stats stats, String warning) {
		logger.debug("start gotoStats()");
		try {
			StatsController statsCtrl = (StatsController) replaceSceneContent("./view/Stats.fxml");
			statsCtrl.setMain(this);
			statsCtrl.displayData(stats, warning);
			stage.show();
		} catch (Exception ex) {
			logger.fatal(ex);
			Platform.exit();
		}
	}

	/**
	 * Assure la présence des fichiers de travail
	 * 
	 * @throws IOException
	 */
	public static void initConfig() throws IOException {
		logger.debug("start initConfig()");
		new File(ProjectParams.getProperty(ProjectParams.LOCALDATA_FILE_KEY)).createNewFile();
	}

	/**
	 * Démarre l'application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.debug("**********************************************************");
		logger.debug("*************** Initializing DOYOUWAR ... ****************");
		logger.debug("**********************************************************");

		try {
			initConfig();
		} catch (IOException e) {
			logger.fatal(e);
			Platform.exit();
		}

		if (!APP_MODE_PROD.equalsIgnoreCase(ProjectParams.getProperty(ProjectParams.APP_MODE_KEY))) {
			Utils.reinitStatsLocally();
		}

		Locale.setDefault(Locale.ENGLISH);
		launch(args);
	}
}
