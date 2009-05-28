package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.MessageBox;

public class GestionProjet extends VerticalPanel {
	
	final private XmlReader reader = new XmlReader("projet", new RecordDef(  
            new FieldDef[]{  
            		new StringFieldDef("titre"),
					new StringFieldDef("idBranche"),
					new StringFieldDef("synopsis"),
					new StringFieldDef("ajouteLe"),
					new StringFieldDef("responsables"),
					new StringFieldDef("auteurs"),
					new StringFieldDef("motscle"),
					new StringFieldDef("nomArchive"),
            }
    ));
	
	private Store store = new Store(reader);

	/**
	 * Constructeur
	 */
	public GestionProjet () {
		FlexTable table = new FlexTable();
		setWidth("100%");
		setStyleName("margeSup20");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		table.setWidget(0, 0, new ListeDeProjets(store, "Liste en cours"));
		add(table);
		chargementProjets();
	}
	
	/**
	 * Charge les projets dans la liste des projets
	 */
	private void chargementProjets () {
		AjaxRequest request = new AjaxRequest("php/GestionProjet.php");
		request.addParameter("action", "listerTousLesProjets");
		
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
}
