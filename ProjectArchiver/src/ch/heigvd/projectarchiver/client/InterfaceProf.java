package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.Authentification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class InterfaceProf extends VerticalPanel {
	
	private static InterfaceProf instance; // L'interface est un Singleton
	private String nomProfesseur; // Le nom du professeur actuellement connecté
	private Toolbar toolbar; // La barre d'outil de l'interface
	
	/**
	 * Constructeur
	 * @param nomProfesseur Le nom du professeur connecté
	 */
	protected InterfaceProf(String nomProfesseur) {
		setWidth("100%");
		this.nomProfesseur = nomProfesseur;
		toolbar = construireToolbar();
		add(toolbar);
		add(new AccueilProf());
		History.newItem("Accueil");
		
		// Gestion de l'historique
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {
				if (event.getValue().equals("Accueil")) {
					changerVue(new AccueilProf());
				}
				else if (event.getValue().equals("AjoutProjet")) {
					changerVue(new AjoutProjet());
				}
				else if (event.getValue().equals("TousLesProjets")) {
					changerVue(new TousLesProjet());
				}
				else if (event.getValue().equals("AjoutCours")) {
					changerVue(new AjoutCours());
				}
			}
			
		});
	}
	
	/**
	 * Crée la première instance de l'interface (Singleton)
	 * @param nomProfesseur Le nom du professeur connecté à l'interface
	 */
	public static void createInstance (String nomProfesseur) {
		instance = new InterfaceProf(nomProfesseur);
	}
	
	/**
	 * Obtient une instance de l'interface (Singleton)
	 * @return L'instance de l'interface
	 */
	public static InterfaceProf getInstance () {
		return instance;
	}
	
	/**
	 * Permet de détruire l'instance de l'interface (qui est utilisée
	 * pour savoir si une session prof est ouverte. L'instance doit
	 * donc être supprimée lors de la fermeture de session)
	 */
	public static void supprimerInstance () {
		instance = null;
	}
	
	/**
	 * @param vue La vue que l'on désire afficher
	 */
	public void changerVue (Widget vue) {
		clear();
		add(toolbar);
		add(vue);
	}
	
	/**
	 * @return Le nom du professeur connecté
	 */
	public String getNomProfesseur () {
		return nomProfesseur;
	}
	
	/**
	 * @return La toolbar de l'interface
	 */
	public Toolbar construireToolbar () {
		toolbar = new Toolbar();
		toolbar.setWidth("100%");
		
		// Ajout d'un menu
		ToolbarButton boutonMenu = new ToolbarButton("Action");
		Menu menu = new Menu();
		boutonMenu.setMenu(menu);
		
		// Menu pour revenir à l'accueil
		Item itemAccueil = new Item("Revenir à l'accueil");
		itemAccueil.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				History.newItem("Accueil");
				changerVue(new AccueilProf());
			}
		});
		menu.addItem(itemAccueil);
		
		// Menu pour ajouter un projet
		Item itemAjouterProjet = new Item("Ajouter un projet");
		itemAjouterProjet.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				History.newItem("AjoutProjet");
				changerVue(new AjoutProjet());
			}
		});
		menu.addItem(itemAjouterProjet);
		
		// Menu pour gérer les projets
		Item itemGererProjets = new Item("Afficher tous les projets");
		itemGererProjets.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				History.newItem("TousLesProjets");
				changerVue(new TousLesProjet());
			}
		});
		menu.addItem(itemGererProjets);
		
		// Menu pour chercher un projet
		Item itemChercherProjet = new Item("Chercher un projet");
		itemChercherProjet.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				changerVue(new Recherche());
			}
		});
		menu.addItem(itemChercherProjet);		
		
		// Menu gérer les branches
		Item itemGererBranches = new Item("Gérer les cours");
		itemGererBranches.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				History.newItem("AjoutCours");
				changerVue(new AjoutCours());
			}
		});
		menu.addItem(itemGererBranches);
		
		// menu Gérer voir la base en XML/XSLT
		Item itemXMLXSLT = new Item("Ouvrir la base en XML/XSLT");
		itemXMLXSLT.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				Window.open(GWT.getModuleBaseURL() + "../xml/projets.xml", "Gestion projet - XML", "");
			}
		});
		menu.addItem(itemXMLXSLT);
		
		toolbar.addButton(boutonMenu);

		// Ajout d'un bouton pour fermer la session
		toolbar.addSeparator();
		ToolbarButton boutonFermerSession = new ToolbarButton("Fermer la session " + nomProfesseur);
		boutonFermerSession.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				new Authentification().seDeconnecter();
			}
		});
		toolbar.addButton(boutonFermerSession);
		
		return toolbar;
	}
}
