package ch.heigvd.projectarchiver.client;
import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;

public class Recherche extends Panel{
	
	
	
	
	final private XmlReader reader = new XmlReader("projet", new RecordDef(  
            new FieldDef[]{  
            		new StringFieldDef("@id"),
            		new StringFieldDef("titre"),
					new StringFieldDef("branche"),
					new StringFieldDef("synopsis"),
					new StringFieldDef("ajouteLe"),
					new StringFieldDef("responsables"),
					new StringFieldDef("auteurs"),
					new StringFieldDef("motsCle"),
					new StringFieldDef("nomArchive"),
            }
    ));
	
	private final Label label_titre = new Label("Titre");
	private final TextField filtre_titre = new TextField();
	
	private final Label label_auteur = new Label("Auteur");
	private final TextField filtre_auteur = new TextField();
	
	private final Label label_cours = new Label("Cours");
	private final TextField filtre_cours = new TextField();
	
	
	private final Label label_Responsable = new Label("Responsable");
	private final TextField filtre_Responsable = new TextField();
	
	private final Label label_motCle = new Label("Mot clé");
	private final TextField filtre_motCle = new TextField();
	
	private final Label label_annee = new Label("Année");
	private final TextField filtre_annee = new TextField();
	
	
	private final Button btnRechercher = new Button("Rechercher");
	private final Panel resultats = new Panel();
	
	FlexTable table = new FlexTable();
	private Store store = new Store(reader);
	
	
	/**
	 * Charge les projets dans la liste des projets
	**/
	private void recherche(String titre, 
						   String auteur,
						   String cours,
						   String responsable, 
						   String motCle, 
						   String annee) {
		
		AjaxRequest request = new AjaxRequest("php/GestionProjet.php");
		
//	
//		titre = titre==""?"###":titre;
//		auteur = auteur==""?"###":auteur;
//		cours = cours==""?"###":cours;
//		responsable = responsable==""?"###":responsable;
//		motCle = motCle==""?"###":motCle;
//		annee = annee==""?"###":annee;
		
		
		
		request.addParameter("action", "filtrerLesProjets");
		request.addParameter("f_titre", titre);
		request.addParameter("f_auteur", auteur);
		request.addParameter("f_cours", cours);
		request.addParameter("f_responsable", responsable);
		request.addParameter("f_motCle", motCle);
		request.addParameter("f_annee", annee);
		
		
		try {
			request.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Une erreur est survenue durant le chargement des projets depuis le fichier XML.");
				}

				public void onResponseReceived(Request request, Response response) {
					store.loadXmlData(response.getText(), false);
				}
				
			});
		} catch (Exception e) {
			MessageBox.alert("Une erreur est survenue durant le chargement des projets depuis le fichier XML.");
		}
		
	}
	
	
	public Recherche(){
		setBorder(true);
		setPaddings(15);
		setTitle("Recherche de projets");
		setWidth(300);
		setShadow(true);
		setCollapsible(true);
		setWidth("100%");
		setHeight("1000px");
				
		// Ajoute les composants au formulaire
		HorizontalPanel controles = new HorizontalPanel();

		controles.add(label_titre);
		controles.add(filtre_titre);
		
		controles.add(label_auteur);
		controles.add(filtre_auteur);
		
		controles.add(label_cours);
		controles.add(filtre_cours);
		
		controles.add(label_Responsable);
		controles.add(filtre_Responsable);
		
		controles.add(label_motCle);
		controles.add(filtre_motCle);
		
		controles.add(label_annee);
		controles.add(filtre_annee);
		
		controles.add(btnRechercher);

		
		
		add(controles);
		
		
		
		
		table.setWidget(0, 0, new ListeDeProjets(store, "Liste des projets"));
		add(table);
		
		
		
		add(resultats);
		
		btnRechercher.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				recherche(filtre_titre.getText(),
						  filtre_auteur.getText(),
						  filtre_cours.getText(),
						  filtre_Responsable.getText(),
						  filtre_motCle.getText(),
						  filtre_annee.getText());
			}
		});		
	}	
}
