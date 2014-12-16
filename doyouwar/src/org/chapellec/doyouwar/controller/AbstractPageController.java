/**
 * 
 */
package org.chapellec.doyouwar.controller;

import java.net.URL;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * @author Cédric Chapelle
 *
 */
public class AbstractPageController implements PageController {

	protected final ReadOnlyObjectWrapper<URL> selectedView = new ReadOnlyObjectWrapper<URL>(this,
			"selectedView");

	/**
	 * @see org.chapellec.doyouwar.controller.PageController#selectedViewProperty()
	 */
	@Override
	public ReadOnlyObjectProperty<URL> selectedViewProperty() {
		return selectedView.getReadOnlyProperty();
	}

	/**
	 * @see org.chapellec.doyouwar.controller.PageController#getSelectedView()
	 */
	@Override
	public URL getSelectedView() {
		return selectedView.get();
	}
}
