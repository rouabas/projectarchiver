package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.Authentification;

import com.google.gwt.core.client.GWT;
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
				changerVue(new AccueilProf());
			}
		});
		menu.addItem(itemAccueil);
		
		// Menu pour ajouter un projet
		Item itemAjouterProjet = new Item("Ajouter un projet");
		itemAjouterProjet.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				changerVue(new AjoutProjet());
			}
		});
		menu.addItem(itemAjouterProjet);
		
		// Menu pour gérer les projets
		Item itemGererProjets = new Item("Afficher tous les projets");
		itemGererProjets.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				changerVue(new TousLesProjet());
			}
		});
		menu.addItem(itemGererProjets);
		
		// Menu pour chercher un projet
		Item itemChercherProjet = new Item("Chercher un projet");
		menu.addItem(itemChercherProjet);
		
		// Menu gérer les branches
		Item itemGererBranches = new Item("Gérer les cours");
		itemGererBranches.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
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
