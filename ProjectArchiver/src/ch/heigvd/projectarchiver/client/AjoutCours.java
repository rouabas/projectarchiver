package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.gwtext.client.widgets.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

public class AjoutCours extends Panel{
	
	private final TextField cours = new TextField();
	// le flux XML
	private final XmlReader xml = new XmlReader("cours", new RecordDef(
			new FieldDef[]{
					new StringFieldDef("nom")
			}
	));
	
	// le conteneur pour nos données
	private final Store datas = new Store(xml);	
	
	public AjoutCours(){
		setBorder(true);
		setPaddings(15);
		setTitle("Ajout de cours");
		setWidth(300);
		setShadow(true);
		add(getGrilleBranche());
		add(getChamps());
		setCollapsible(true);
	}
	
	private GridPanel getGrilleBranche(){		

		getXml();
		
		// config des colonnes
		ColumnConfig nomCol = new ColumnConfig("nom", "nom", 195,true);
		
		ColumnConfig[] colonnes = {
			nomCol
		};
		
		ColumnModel columnModel = new ColumnModel(colonnes);
		
		// la grille a ajouter
		GridPanel grille = new GridPanel();
		grille.setTitle("Liste des cours");
		grille.setColumnModel(columnModel);
		grille.setStore(datas);
		grille.setWidth(200);
		grille.setHeight(400);
		grille.setStyleName("grille");
		return grille;
	}
	
	private void getXml(){
		AjaxRequest requete = new AjaxRequest("php/cours.php");
		requete.addParameter("action", "coursXML");
		try {
			requete.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Une erreur inconnue s'est produite durant la récupération des cours.");
				}

				public void onResponseReceived(Request request, Response response) {
					//datas.loadXmlData(response.getText(), true);
					MessageBox.alert(response.getText());
				}
			});
		} catch (RequestException e1) {
			MessageBox.alert("Erreur", "Une erreur inconnue s'est produite durant la récupération des cours.");
		}
	}
	
	private HorizontalPanel getChamps(){
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("210");
		cours.setStyleName("textbouton");
		Button ok = new Button("ok");
		panel.add(cours);
		panel.add(ok);
		
		// Action du bouton de ok
		ok.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				ajouterCours();
			}
		});
		
		// Action sur le champs de la branche
		cours.addKeyPressListener(new EventCallback() {

			public void execute(EventObject e) {
				if (e.getKey() == KeyCodes.KEY_ENTER)
					ajouterCours();
			}
		});		
		
		return panel;
	}
	
	private void ajouterCours(){
		AjaxRequest requete = new AjaxRequest("php/cours.php");
		requete.addParameter("action", "ajouter");
		requete.addParameter("cours", cours.getText());
		try {
			requete.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Une erreur inconnue s'est produite durant l'ajout du cours");
				}

				public void onResponseReceived(Request request, Response response) {
					// Vérification de la réponse
					if (response.getText().equals("!exist"))
						MessageBox.alert("Le cours existe déjà");
					// Réponse OK
					else {
						MessageBox.alert(response.getText());
						//MessageBox.alert("Cours ajouté!");
					}		
				}
			});
		} catch (RequestException e1) {
			MessageBox.alert("Erreur", "Une erreur inconnue s'est produite durant l'ajout du cours");
		}
	}
}
