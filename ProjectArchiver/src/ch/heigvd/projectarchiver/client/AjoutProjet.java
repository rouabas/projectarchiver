package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
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

public class AjoutProjet extends ModeleEditionProjet {
	
	private final FileUpload fichier = new FileUpload();
	
	public AjoutProjet() {
		super("Ajout de projet");
		setFormulaire(construireFormulaire());
	}
	
	private void enregistrer() {
		AjaxRequest requete = new AjaxRequest("php/GestionProjet.php");
		requete.addParameter("action", "ajouterProjet");
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
					InterfaceProf.getInstance().changerVue(new AccueilProf());
					MessageBox.alert("Votre projet a été ajouté avec succès!");
				}
			});
		} catch (RequestException e1) {
			MessageBox
					.alert("Erreur",
							"Une erreur inconnue s'est produite durant l'enregistrement.");
		}
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
						|| getAuteurs().equals("")
						|| fichier.getFilename() == "")
					MessageBox.alert("Erreur",
							"Certaines infos n'ont pas été remplies");
				else {
					form.submit();
				}
			}
		});

		fichier.setName("fichier");
		VerticalPanel formPanel = new VerticalPanel();
		
		formPanel.add(new Hidden("action", "uploadFichier"));
		formPanel.add(new Hidden("id", ""));
		formPanel.add(fichier);
		formPanel.add(btnEnregistrer);
		form.setWidget(formPanel);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().trim().equals("!ok"))
					enregistrer();
				else
					MessageBox.alert(event.getSource().toString());
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
