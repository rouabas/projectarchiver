package ch.heigvd.projectarchiver.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Panel;

public class AccueilProf extends VerticalPanel {
	
	/**
	 * Constructeur
	 */
	public AccueilProf() {
		// Construction d'un message d'accueil
		Panel panelBienvenue = new Panel();
		panelBienvenue.setTitle("Espace professeur - Accueil");
		panelBienvenue.add(new HTML("Bienvenue dans l'espace professeur.<BR/><BR/>" +
						   "Cet espace vous permet d'ajouter des projets dans la base de données, " +
						   "d'ajouter votre cours dans la liste des cours et, bien évidemment, de consulter " +
						   "la base de données de projets.<BR/><BR/>" +
						   "Le menu Action en haut à gauche vous permet d'effectuer ces tâches."));
		panelBienvenue.setWidth("500px");
		panelBienvenue.setHeight("200px");
		
		setWidth("100%");
		setStyleName("margeSup20");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		add(panelBienvenue);
	}

}
