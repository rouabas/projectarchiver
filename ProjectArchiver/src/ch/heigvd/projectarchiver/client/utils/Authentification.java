package ch.heigvd.projectarchiver.client.utils;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.TextField;

public class Authentification extends VerticalPanel {
	
	final private TextField champLogin = new TextField();
	final private TextField champMotDePasse = new TextField(); 
	
	public Authentification () {
		setWidth("100%");
		setHeight("100px");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		add(construireTitre());
		add(construireAccueil());
		add(construirePanneauAuth());
	}
	
	public SimplePanel construireTitre () {
		SimplePanel titre = new SimplePanel();
		titre.add(new HTML("<H1>HEIG-VD - Consultation de projet</H1>"));
		titre.setStyleName("margeSup20");
		return titre;
	}
	
	public Panel construireAccueil () {
		Panel panneauAccueil = new Panel();
		panneauAccueil.setStyleName("margeSup20");
		
		VerticalPanel contenu = new VerticalPanel();
		Button boutonEntrer = new Button("Entrer");
		boutonEntrer.setStyleName("margeSup20");
		
		panneauAccueil.setHeight("150px");
		panneauAccueil.setWidth("500px");
		panneauAccueil.setTitle("Bienvenue");
		contenu.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		contenu.add(new HTML("La consultation des projets est ouverte à tous.</BR>Cliquez ci-dessous pour entrer dans l'espace public"));
		contenu.add(boutonEntrer);
		
		panneauAccueil.add(contenu);
		
		return panneauAccueil;
	}
	
	public Panel construirePanneauAuth () {
		Panel panneauAuth = new Panel	();
		VerticalPanel conteneur = new VerticalPanel();
		FlexTable contenu = new FlexTable();
		Button boutonConnexion = new Button("Se connecter");
		
		conteneur.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		boutonConnexion.setStyleName("margeSup20");
		
		panneauAuth.setStyleName("margeSup20");
		panneauAuth.setHeight("200px");
		panneauAuth.setWidth("500px");
		panneauAuth.setPaddings(20, 0, 0, 0);
		panneauAuth.setTitle("Accès espace professeurs");
		
		champMotDePasse.setPassword(true);
		contenu.setWidget(0, 0, new HTML("Nom d'utilisateur"));
		contenu.setWidget(0, 1, new HTML("Mot de passe"));
		contenu.setWidget(1, 0, champLogin);
		contenu.setWidget(1, 1, champMotDePasse);
		
		conteneur.add(contenu);
		conteneur.add(boutonConnexion);
		
		panneauAuth.add(conteneur);
		champLogin.focus(true);
		return panneauAuth;
	}
}
