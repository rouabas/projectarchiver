package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.gwtext.client.widgets.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;

public class RechercheMotCles extends VerticalPanel {

	private final Button btnRechercher = new Button("Rechercher");
	private final TextField txtMotsCles = new TextField();
	
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
	
	private Store store = new Store(reader);

	/**
	 * Constructeur
	 */
	public RechercheMotCles () {
		FlexTable table = new FlexTable();
		setWidth("100%");
		setStyleName("margeSup20");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		btnRechercher.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {	
				if(txtMotsCles.getText()!="")
					rechercheDuProjet();
			}
		});
		
		HorizontalPanel panelControles = new HorizontalPanel();
		panelControles.add(new HTML("Mot clé à rechercher"));
		panelControles.add(txtMotsCles);
		panelControles.add(btnRechercher);
		table.setWidget(0, 0, panelControles);
		table.setWidget(1, 0, new ListeDeProjets(store, "Recherche de projets"));
		add(table);
		
	}
	
	/**
	 * Charge les projets dans la liste des projets
	 */
	private void rechercheDuProjet () {
		AjaxRequest request = new AjaxRequest("php/GestionProjet.php");
		request.addParameter("action", "rechercherProjet");
		request.addParameter("motcle", txtMotsCles.getText());
		try {
			request.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Une erreur est survenue durant le chargement des projets depuis le fichier XML.");
				}

				public void onResponseReceived(Request request, Response response) {
					if(response.getText().equals("!noresult"))
						MessageBox.alert("Aucun projet ne correspond à votre recherche...");
					else
						MessageBox.alert(response.getText());
						//store.loadXmlData(response.getText(), false);
				}
				
			});
		} catch (Exception e) {
			MessageBox.alert("Une erreur est survenue durant le chargement des projets depuis le fichier XML.");
		}
	}	
	
}
