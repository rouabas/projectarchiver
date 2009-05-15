package ch.heigvd.projectarchiver.client.utils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Authentification extends VerticalPanel {
	
	public Authentification () {
		setWidth("100%");
		setHeight("100px");
		add(construireTitre());
	}
	
	public SimplePanel construireTitre () {
		SimplePanel titre = new SimplePanel();
		titre.add(new HTML("<H1>Consultation de projet</H1></BR><H2>Authentification</H2>"));
		return titre;
	}


}
