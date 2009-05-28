package ch.heigvd.projectarchiver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class PageProjet extends VerticalPanel {
	
	private Record infosProjet;
	
	/**
	 * Constructeur
	 * @param infosProjet Les informations sur le projet 
	 */
	public PageProjet (Record infosProjet) {
		Button retour = new Button("Retour");
		this.infosProjet = infosProjet;
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		setWidth("100%");
		retour.setStyleName("margeSup20");
		add(construireTitre());
		add(construireInfos());
		add(retour);
		
		// Action du bouton retour
		retour.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (e.getMouseButton() == 0)
					History.back();
			}
		});
		
		History.newItem("Projet" + infosProjet.getAsString("@id"));
	}
	
	/**
	 * @return Le titre de la page de projet
	 */
	public SimplePanel construireTitre() {
		SimplePanel titlePanel = new SimplePanel();
		titlePanel.setStyleName("margeSup20");
		titlePanel.setWidth("100%");
		HTML titleText = new HTML("<h1>Projet N°" + infosProjet.getAsString("@id") + "<BR/>" + infosProjet.getAsString("titre") + "</h1>");
		titleText.setHorizontalAlignment(HTML.ALIGN_CENTER);
		titlePanel.add(titleText);
		return titlePanel;
	}
	
	/**
	 * @return Le panneau contenant les infos du projet
	 */
	public Panel construireInfos () {
		Panel panel = new Panel();
		panel.setStyleName("margeSup20");
		panel.setWidth("900px");
		panel.setTitle("Informations sur le projet");
		FlexTable contenu = new FlexTable();
		contenu.setCellSpacing(10);
		contenu.getColumnFormatter().setWidth(0, "130px");
		contenu.getColumnFormatter().setWidth(1, "780px");
		
		Button boutonTelecharger = new Button("Télécharger le projet");
		// Action du bouton télécharger
		boutonTelecharger.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (e.getMouseButton() == 0)
					Window.open(GWT.getModuleBaseURL() + "../projectFiles/" + infosProjet.getAsString("@id") + "/" + infosProjet.getAsString("nomArchive"), "Archive du projet", "");
			}
		});
		
		contenu.setWidget(0, 0, new HTML("<b>Auteurs: </b>"));
		contenu.setWidget(0, 1, new HTML(infosProjet.getAsString("auteurs")));
		contenu.setWidget(1, 0, new HTML("<b>Responsables: </b>"));
		contenu.setWidget(1, 1, new HTML(infosProjet.getAsString("responsables")));
		contenu.setWidget(2, 0, new HTML("<b>Cours: </b>"));
		contenu.setWidget(2, 1, new HTML(infosProjet.getAsString("branche")));
		contenu.setWidget(3, 0, new HTML("<b>Mots clés: </b>"));
		contenu.setWidget(3, 1, new HTML(infosProjet.getAsString("motsCle")));
		contenu.setWidget(4, 0, new HTML("<b>Date d'ajout: </b>"));
		contenu.setWidget(4, 1, new HTML(infosProjet.getAsString("ajouteLe")));
		contenu.setWidget(5, 0, new HTML("<b>Synopsis :</b>"));
		contenu.setWidget(5, 1, new HTML(infosProjet.getAsString("synopsis")));
		contenu.setWidget(6, 0, new HTML("<b>Archive: </b>"));
		contenu.setWidget(6, 1, boutonTelecharger);
		
		panel.add(contenu);
		return panel;
	}
}
