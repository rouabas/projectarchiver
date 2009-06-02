package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class EditionProjet extends ModeleEditionProjet {
	
	private Record infosProjet;
	private final FileUpload fichier = new FileUpload();
	
	/**
	 * Constructeur
	 * @param infosProjet Les informations sur le projet 
	 */
	public EditionProjet (final Record infosProjet) {
		//super("Modification du projet n°" + infosProjet.getAsString("@id"), new Hidden[]{new Hidden("id", infosProjet.getAsString("@id"))});
		super("Modification du projet n°" + infosProjet.getAsString("@id"));
		this.infosProjet = infosProjet;
		chargerDonneesProjet();
		setFormulaire(construireFormulaire());
		Button retour = new Button("Retour");
		retour.setStyleName("margeSup20");
		
		// Action du bouton retour
		retour.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (e.getMouseButton() == 0)
					InterfaceProf.getInstance().changerVue(new AccueilProf());
			}
		});
		
		add(retour);
	}
	
	private void enregistrer() {
		AjaxRequest requete = new AjaxRequest("php/GestionProjet.php");
		requete.addParameter("action", "modifierProjet");
		requete.addParameter("id", infosProjet.getAsString("@id"));
		requete.addParameter("titre", getTitre());
		requete.addParameter("idBranche", getBranche());
		requete.addParameter("synopsis", getSynopsis());
		requete.addParameter("responsables", getResponsables());
		requete.addParameter("auteurs", getAuteurs());
		requete.addParameter("motsCle", getMotsCles());
		requete.addParameter("nomArchive", getNomFichier());
		try {
			requete.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox
							.alert("Erreur",
									"Une erreur inconnue s'est produite durant l'enregistrement.");
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getText().trim().equals("!ok")) {
						InterfaceProf.getInstance().changerVue(new PageProjet(infosProjet));
						MessageBox.alert("Votre projet a été modifié avec succès!");
					}
					else
						MessageBox.alert(response.getText());
				}
			});
		} catch (RequestException e1) {
			MessageBox
					.alert("Erreur",
							"Une erreur inconnue s'est produite durant l'enregistrement.");
		}
	}
	
	private void chargerDonneesProjet() {
		chargerTitre(infosProjet.getAsString("titre"));
		chargerResponsables(infosProjet.getAsString("responsables"));
		chargerAuteurs(infosProjet.getAsString("auteurs"));
		chargerMotsCles(infosProjet.getAsString("motsCle"));
		chargerSynopsis(infosProjet.getAsString("synopsis"));
		chargerCours(infosProjet.getAsString("branche"));
	}
	
	/**
	 * Construit le formulaire html pour modifier le projet
	 */
	private FormPanel construireFormulaire () {
		final FormPanel form = new FormPanel();
		
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL() + "../php/GestionProjet.php");

		Button btnEnregistrer = new Button("Enregistrer");
		btnEnregistrer.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {				
				if (getTitre().equals("")
						|| getResponsables().equals("")
						|| getAuteurs().equals(""))
					MessageBox.alert("Erreur",
							"Hormis le fichier, tous les champs doivent être remplis.");
				else {
					// On regarde si un nouveau fichier doit être uploader
					if (getNomFichier().equals(""))
						enregistrer();
					else
						form.submit();
				}
			}
		});

		fichier.setName("fichier");
		VerticalPanel formPanel = new VerticalPanel();
		
		formPanel.add(new Hidden("action", "uploadFichier"));
		formPanel.add(new Hidden("id", infosProjet.getAsString("@id")));
		formPanel.add(fichier);
		formPanel.add(btnEnregistrer);
		form.setWidget(formPanel);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			//@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().trim().equals("!ok"))
					enregistrer();
				else
					MessageBox.alert(event.getResults());
			}
		});
		
		return form;
	}
	
	/**
	 * @return Le nom du fichier uploadé
	 */
	public String getNomFichier () {
		if (fichier.getFilename().equals(""))
			return "";
		else {
		String[] nomArchiveSepare = fichier.getFilename().split("/");
		return nomArchiveSepare[nomArchiveSepare.length-1];
		}
	}

}
