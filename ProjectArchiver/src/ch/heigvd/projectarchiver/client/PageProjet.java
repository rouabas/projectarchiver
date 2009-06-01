package ch.heigvd.projectarchiver.client;


import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.NameValuePair;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class PageProjet extends VerticalPanel {
	
	private Record infosProjet;
	
	/**
	 * Constructeur
	 * @param infosProjet Les informations sur le projet 
	 */
	public PageProjet (Record infosProjet) {
		this.infosProjet = infosProjet;
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		setWidth("100%");
		add(construireTitre());
		add(construireInfos());
		add(construireBoutons());
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
	
	/**
	 * @return Les boutons retour, modifier et supprimer (ces deux derniers n'apparaîssent que
	 * si c'est un professeur qui accède à la page).
	 */
	public HorizontalPanel construireBoutons () {
		HorizontalPanel container = new HorizontalPanel();
		container.setStyleName("margeSup20");
		container.setSpacing(20);
		Button retour = new Button("Retour");
		Button modifier;
		Button supprimer;
		
		// Action du bouton retour
		retour.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (e.getMouseButton() == 0)
					History.back();
			}
		});
		
		container.add(retour);
		
		// Si une session admin est ouverte, on affiche les boutons pour modifier
		// et supprimer le projet
		if (InterfaceProf.getInstance() != null) {
			modifier = new Button("Modifier");
			supprimer = new Button("Supprimer");
			
			modifier.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					if (e.getMouseButton() == 0)
						InterfaceProf.getInstance().changerVue(new EditionProjet(infosProjet));
				}
			});
			
			// Action du bouton supprimer
			supprimer.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					if (e.getMouseButton() == 0) {
						// Prépartion du message de confirmation
						MessageBoxConfig alertConfig = new MessageBoxConfig();
						NameValuePair[] alertNameValuePair = {new NameValuePair("yes","oui"),
								           new NameValuePair("no","non")};
						alertConfig.setButtons(alertNameValuePair);
						alertConfig.setTitle("Suppression de projet");
						alertConfig.setMsg("Etes-vous sur de vouloir supprimer ce projet?");
						alertConfig.setCallback(new PromptCallback() {
							public void execute(String btnID, String text) {
								// La suppression est faite si le professeur confirme la suppression
								if (btnID.equals("yes")) {
									/*AjaxRequest request = new AjaxRequest("php/GestionProjet.php");
									request.addParameter("action", "supprimerProjet");
									request.addParameter("idProjet", infosProjet.getAsString("@id"));
									try {
										request.send(new RequestCallback() {

											public void onError(Request request, Throwable exception) {
												MessageBox.alert("Une erreur est survenue lors de la suppression. Veuillez réessayer");												
											}

											public void onResponseReceived( Request request, Response response) {
												InterfaceProf.getInstance().changerVue(new AccueilProf());
												MessageBox.alert("Le projet a été supprimé");
											}
											
										});
									} catch (Exception e) {
										MessageBox.alert("Une erreur est survenue lors de la suppression. Veuillez réessayer");
									}*/
								}
							}
						});
						MessageBox.show(alertConfig);
					}
				}
			});
			
			container.add(supprimer);
			container.add(modifier);
		}
		
		return container;
	}
}
