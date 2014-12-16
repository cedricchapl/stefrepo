/**
 * 
 */
package org.chapellec.doyouwar.controller;

import java.net.URL;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * @author Cédric Chapelle
 *
 */
public interface SubMenuController {
	/**
	 * 
	 * @return
	 */
	public ReadOnlyObjectProperty<URL> selectedViewProperty();

	/**
	 * 
	 * @return
	 */
	public URL getSelectedView();
}