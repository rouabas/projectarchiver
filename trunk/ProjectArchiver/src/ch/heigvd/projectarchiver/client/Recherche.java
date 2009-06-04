package ch.heigvd.projectarchiver.client;
import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventCallback;
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

public class Recherche extends VerticalPanel{

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
	
	private final Label labelCritere = new Label("Critère");
	private final TextField filtreCritere = new TextField();
		
	private final Button btnRechercher = new Button("Rechercher");
	
	FlexTable table = new FlexTable();
	private Store store = new Store(reader);
	
	
	/**
	 * Charge les projets dans la liste des projets
	**/
	private void recherche(String critere) {
		
		AjaxRequest request = new AjaxRequest("php/GestionProjet.php");
		
		request.addParameter("action", "filtrerLesProjets");
		request.addParameter("critere", critere);
		
		
		try {
			request.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Une erreur est survenue durant le chargement des projets depuis le fichier XML.");
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getText().trim().equals("!aucun")) {
						MessageBox.alert("Aucun résulat n'a été trouvé avec ce critère");
						store.removeAll();
					}
						
					else
						store.loadXmlData(response.getText(), false);
				}
				
			});
		} catch (Exception e) {
			MessageBox.alert("Une erreur est survenue durant le chargement des projets depuis le fichier XML.");
		}
		
	}
	
	
	public Recherche(){
		setStyleName("margeSup20");
		setWidth("100%");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
				
		// Ajoute les composants au formulaire
		HorizontalPanel controles = new HorizontalPanel();

		// Validation par return dans le champ de recherche
		filtreCritere.addKeyPressListener(new EventCallback() {

			public void execute(EventObject e) {
				if (e.getKey() == KeyCodes.KEY_ENTER)
					btnRechercher.fireEvent("click");
			}
		});
		
		controles.add(labelCritere);
		controles.add(filtreCritere);		
		controles.add(btnRechercher);
	
		add(controles);

		table.setStyleName("margeSup20");
		table.setWidget(0, 0, new ListeDeProjets(store, "Liste des projets"));
		table.setWidth("900px");
		add(table);
		
		btnRechercher.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				recherche(filtreCritere.getText());
			}
		});		
	}	
}
