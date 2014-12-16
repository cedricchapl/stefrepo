package org.chapellec.doyouwar;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Lanceur de l'application
 * 
 * @author Cédric Chapelle
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("./view/Main.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("DOYOUWAR ?");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Assure la présence des fichiers de travail 
	 * @throws IOException
	 */
	public static void initConfig () throws IOException {
		new File(ProjectParams.getProperty(ProjectParams.SERVERDATA_FILE_KEY)).createNewFile();
		new File(ProjectParams.getProperty(ProjectParams.LOCALDATA_FILE_KEY)).createNewFile();		
	}
	
	/**
	 * Démarre l'application 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			initConfig();
			launch(args);
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
}
