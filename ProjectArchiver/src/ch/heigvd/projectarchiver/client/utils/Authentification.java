package ch.heigvd.projectarchiver.client.utils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.portal.Portlet;

public class Authentification extends VerticalPanel {
	
	public Authentification () {
		setWidth("100%");
		setHeight("100px");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		add(construireTitre());
		add(construireContenu());
	}
	
	public SimplePanel construireTitre () {
		SimplePanel titre = new SimplePanel();
		titre.add(new HTML("<H1>Consultation de projet</H1>"));
		return titre;
	}
	
	public Portlet construireContenu () {
		Portlet portlet = new Portlet("test", "cool");
	    return portlet;
	}
}
