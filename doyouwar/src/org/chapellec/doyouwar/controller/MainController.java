package org.chapellec.doyouwar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Contrôleur principal
 * 
 * @author Cédric Chapelle
 *
 */
public class MainController implements Initializable {

	private AbstractPageController currentPageController;

	final private static URL home = MainController.class.getResource("../view/Home.fxml");

	final private static URL faq = MainController.class.getResource("../view/Faq.fxml");

	final private static URL helpUs = MainController.class.getResource("../view/HelpUs.fxml");

	final private static URL about = MainController.class.getResource("../view/About.fxml");

	@FXML
	private BorderPane mainPane;

	/**
	 * 
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		mainPane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

		try {
			homeAction();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// selectedView property of currentPage:
		ObservableValue<URL> pageSelectedView = Bindings.<URL> select(currentPageController,
				"selectedView");
		// pageSelectedView.addListener(new ChangeListener<URL>() {
		currentPageController.selectedViewProperty().addListener(new ChangeListener<URL>() {
			public void changed(ObservableValue<? extends URL> obs, URL oldView, URL newView) {
				System.out.println(this.getClass() + " : selectedView value=" + newView);
				if (newView == null) {
					mainPane.setCenter(null);
				} else {
					FXMLLoader loader = new FXMLLoader(newView);
					Parent view = null;
					try {
						view = (Parent) loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mainPane.setCenter(view);
				}
			}
		});
	}

	/**
	 * handler method for Home button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void homeAction() throws IOException {
		changePageController(home);
	}

	/**
	 * handler method for About button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void aboutAction() throws IOException {
		changePageController(about);
	}

	/**
	 * handler method for FAQ button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void faqAction() throws IOException {
		changePageController(faq);
	}

	/**
	 * handler method for HELP US button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void helpUsAction() throws IOException {
		changePageController(helpUs);
	}

	private void changePageController(URL fxml) throws IOException {
		FXMLLoader loader = new FXMLLoader(fxml);
		Parent center = (Parent) loader.load();
		mainPane.setCenter(center);
		AbstractPageController ctrl = (AbstractPageController) loader.getController();
		currentPageController = ctrl;
	}
}
