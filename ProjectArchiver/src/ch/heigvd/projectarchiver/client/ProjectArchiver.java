package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.Authentification;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ProjectArchiver implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Authentification authentification = new Authentification();
		RootPanel.get().add(authentification);
	}
}
