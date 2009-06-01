package ch.heigvd.projectarchiver.client.utils;

import ch.heigvd.projectarchiver.client.InterfaceProf;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;

public class Authentification extends VerticalPanel {
	
	final private TextField champLogin = new TextField();
	final private TextField champMotDePasse = new TextField(); 
	
	public Authentification () {
		setWidth("100%");
		setHeight("100px");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		demarrerLogin();
	}
	
	/**
	 * Vérifie si une session existe et sinon affiche la page de login
	 */
	private void demarrerLogin () {
		// On vérifie si une session existe déjà
		AjaxRequest request = new AjaxRequest("php/Authentification.php");
		request.addParameter("action", "estConnecte");
		
		try {
			request.send(new RequestCallback() {
	
				// Une erreur s'est produite, on affiche la page de login
				public void onError(Request request, Throwable exception) {
					afficherPageLogin();
				}
	
				public void onResponseReceived(Request request, Response response) {
					// Si la session est déjà ouverte, on affiche l'interface
					if (!response.getText().trim().equals("false")) {
						InterfaceProf.createInstance(response.getText());
						RootPanel.get().clear();
						RootPanel.get().add(InterfaceProf.getInstance());
					}
					// Sinon => page de login
					else
						afficherPageLogin();
				}
			});
		}
		// Une erreur s'est produite, on affiche la page de login
		catch (RequestException e) {
			afficherPageLogin();
		}
	}
	
	/**
	 * Affiche la page de login
	 */
	public void afficherPageLogin () {
		add(construireTitre());
		add(construireAccueil());
		add(construirePanneauAuth());
		champLogin.focus(true, true);
	}
	
	/**
	 * @return Le panneau contenant le titre
	 */
	private SimplePanel construireTitre () {
		SimplePanel titre = new SimplePanel();
		titre.add(new HTML("<H1>HEIG-VD - Consultation de projet</H1>"));
		titre.setStyleName("margeSup20");
		return titre;
	}
	
	/**
	 * @return Le panneau contenant le message de bienvenue et le lien pour entrer dans l'espace public
	 */
	private Panel construireAccueil () {
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
	
	/**
	 * @return Le panneau contenant le formulaire d'authentification pour accéder à l'espace des professeurs
	 */
	private Panel construirePanneauAuth () {
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
		
		// Action du bouton de connexion
		boutonConnexion.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				seConnecter();
			}
		});
		
		// Action sur le champs du login (validation par return)
		champLogin.addKeyPressListener(new EventCallback() {

			public void execute(EventObject e) {
				if (e.getKey() == KeyCodes.KEY_ENTER)
					seConnecter();
			}
		});
		// Action sur le champs du mot de passe (validation par return)
		champMotDePasse.addKeyPressListener(new EventCallback() {

			public void execute(EventObject e) {
				if (e.getKey() == KeyCodes.KEY_ENTER)
					seConnecter();
			}
		});
		
		panneauAuth.add(conteneur);
		return panneauAuth;
	}
	
	/**
	 * Ferme la session et affiche la fenêtre de login
	 */
	public void seDeconnecter () {
		AjaxRequest requete = new AjaxRequest("php/authentification.php");
		requete.addParameter("action", "seDeconnecter");
		try {
			requete.send(new RequestCallback() {
				public void onError(Request request, Throwable exception) {}
				public void onResponseReceived(Request request, Response response) {}
			});
		} catch (RequestException e1) {}
		
		RootPanel.get().clear();
		RootPanel.get().add(this);
		InterfaceProf.supprimerInstance();
		
	}
	
	/**
	 * Essaie de se connecter à l'espace professeur
	 */
	private void seConnecter () {
		AjaxRequest requete = new AjaxRequest("php/Authentification.php");
		requete.addParameter("action", "authentification");
		requete.addParameter("login", champLogin.getText());
		requete.addParameter("motDePasse", champMotDePasse.getText());
		try {
			requete.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Une erreur inconnue s'est produite durant l'authentification");
				}

				public void onResponseReceived(Request request, Response response) {
					// Vérification de la réponse
					if (response.getText().equals("!false"))
						MessageBox.alert("Nom d'utilisateur ou mot de passe incorrect");
					else if (response.getText().equals("!erreurLDAP"))
						MessageBox.alert("Erreur LDAP", "Une erreur inconnue s'est produite durant la connexion au LDAP." +
						"<BR/>Veuillez noter que ce site n'est accessible que depuis la HEIG-VD ou à travers le VPN.");
					// Réponse OK
					else {
						InterfaceProf.createInstance(response.getText());
						RootPanel.get().clear();
						RootPanel.get().add(InterfaceProf.getInstance());
					}
						
				}
				
			});
		} catch (RequestException e1) {
			MessageBox.alert("Erreur", "Une erreur inconnue s'est produite durant l'authentification." +
					"<BR/>Veuillez noter que ce site n'est accessible que depuis la HEIG-VD ou à travers le VPN.");
		}
	}
}
