package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;

public class AjoutProjet extends VerticalPanel {

	private final TextField titre = new TextField();
	private final TextArea responsable = new TextArea();
	private final TextArea auteurs = new TextArea();
	private final TextArea motsCles = new TextArea();
	private final TextArea synopsis = new TextArea();
	
	private final FormPanel form = new FormPanel();
	private final FileUpload fichier = new FileUpload();
	
	private final int LARGEURTEXTAREA = 300;
	private final String SEPARATEUR = ";";
	
	private final TextField txtAjoutResp = new TextField();
	private final TextField txtAjoutAuth = new TextField();
	private final TextField txtAjoutMC = new TextField();
	
	// le flux XML
	private final XmlReader xml = new XmlReader("cours", new RecordDef(
			new FieldDef[]{
					new StringFieldDef("nom"),
			}
	));
	
	// le conteneur pour nos données
	private final Store datas = new Store(xml);	
	
	private ListBox listeCours = new ListBox();
	
	public AjoutProjet() {
		setWidth("100%");
		setStyleName("margeSup20");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		Panel panelProjet = new Panel();
		panelProjet.setBorder(true);
		panelProjet.setPaddings(15);
		panelProjet.setTitle("Ajout de projet");
		panelProjet.setWidth(500);
		panelProjet.setShadow(true);
		
		FlexTable formulaire = new FlexTable();
		formulaire.setWidget(0, 0, new HTML("Cours"));
		formulaire.setWidget(1, 0, new HTML("Titre"));
		HTML htmlResp = new HTML("Responsables");
		htmlResp.setHeight("130");
		formulaire.setWidget(2, 0, htmlResp);
		HTML htmlAuth = new HTML("Auteurs");
		htmlAuth.setHeight("130");
		formulaire.setWidget(3, 0, htmlAuth);
		HTML htmlMC = new HTML("Mots clés");
		htmlMC.setHeight("130");		
		formulaire.setWidget(4, 0, htmlMC);
		HTML htmlSyno = new HTML("Synopsis");
		htmlSyno.setHeight("200");
		formulaire.setWidget(5, 0, htmlSyno);
		
		
		getXml();

		formulaire.setWidget(0, 1, listeCours);
		formulaire.setWidget(1, 1, titre);
		
		responsable.setHeight("100");
		responsable.setWidth(String.valueOf(LARGEURTEXTAREA));
		responsable.setReadOnly(true);
		VerticalPanel panelResponsables = new VerticalPanel();
		panelResponsables.add(responsable);
		HorizontalPanel panelRespTxtBtn = new HorizontalPanel();
		Button btnAjoutResp = new Button("+");
		// Action du bouton de ok
		btnAjoutResp.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if(txtAjoutResp.getText()!=""){
					if(responsable.getText()=="")
						responsable.setText(txtAjoutResp.getText());
					else
						responsable.setText(responsable.getText()+SEPARATEUR+txtAjoutResp.getText());
				}
				txtAjoutResp.setValue("");
				txtAjoutResp.focus();
			}
		});		
		panelRespTxtBtn.add(txtAjoutResp);
		panelRespTxtBtn.add(btnAjoutResp);
		panelResponsables.add(panelRespTxtBtn);
		formulaire.setWidget(2, 1, panelResponsables);
		
		auteurs.setHeight("100");
		auteurs.setWidth(String.valueOf(LARGEURTEXTAREA));
		auteurs.setReadOnly(true);
		VerticalPanel panelAuteurs = new VerticalPanel();
		panelAuteurs.add(auteurs);
		HorizontalPanel panelAuthTxtBtn = new HorizontalPanel();
		Button btnAjoutAuth = new Button("+");
		// Action du bouton de ok
		btnAjoutAuth.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if(txtAjoutAuth.getText()!=""){
					if(auteurs.getText()=="")
						auteurs.setText(txtAjoutAuth.getText());
					else
						auteurs.setText(auteurs.getText()+SEPARATEUR+txtAjoutAuth.getText());
				}
				txtAjoutAuth.setValue("");
				txtAjoutAuth.focus();
			}
		});		
		panelAuthTxtBtn.add(txtAjoutAuth);
		panelAuthTxtBtn.add(btnAjoutAuth);
		panelAuteurs.add(panelAuthTxtBtn);
		formulaire.setWidget(3, 1, panelAuteurs);
		
		motsCles.setHeight("100");
		motsCles.setWidth(String.valueOf(LARGEURTEXTAREA));
		motsCles.setReadOnly(true);
		VerticalPanel panelMotCles = new VerticalPanel();
		panelMotCles.add(motsCles);
		HorizontalPanel panelMCTxtBtn = new HorizontalPanel();
		Button btnAjoutMC = new Button("+");
		// Action du bouton de ok
		btnAjoutMC.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if(txtAjoutMC.getText()!=""){
					if(motsCles.getText()=="")
						motsCles.setText(txtAjoutMC.getText());
					else
						motsCles.setText(motsCles.getText()+SEPARATEUR+txtAjoutMC.getText());
				}
				txtAjoutMC.setValue("");
				txtAjoutMC.focus();
			}
		});		
		panelMCTxtBtn.add(txtAjoutMC);
		panelMCTxtBtn.add(btnAjoutMC);
		panelMotCles.add(panelMCTxtBtn);
		formulaire.setWidget(4, 1, panelMotCles);
		
		synopsis.setHeight("200");
		synopsis.setWidth(String.valueOf(LARGEURTEXTAREA));
		formulaire.setWidget(5, 1, synopsis);
		
		

		
		//formulaire.setWidget(6, 1, fichier);
		
		
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
		form.setAction("php/GestionProjet.php");
		form.add(new Hidden("action", "test"));
	    
		Button btnEnregistrer = new Button("Enregistrer");
		btnEnregistrer.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				if(titre.getValueAsString()=="" ||
						responsable.getValue() == ""||
						auteurs.getValue()==""||
						fichier.getFilename()=="")
					MessageBox.alert("Erreur", "Certaines infos n'ont pas été remplies");
				else{
					form.submit();
					//enregistrer();
				}
			}
		});

		fichier.setName("fichier");
	    VerticalPanel formPanel = new VerticalPanel();
	    formPanel.add(fichier);
	    formPanel.add(btnEnregistrer);
	    form.setWidget(formPanel);
	    
	
	    form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				//MessageBox.alert(event.getResults());
				enregistrer();
			}
	    });

	    
	    
	    formulaire.setWidget(6, 1, form);
		
		//formulaire.setWidget(7, 1, btnEnregistrer);
		
		panelProjet.add(formulaire);
		add(panelProjet);
	}

	private void enregistrer(){
		AjaxRequest requete = new AjaxRequest("php/GestionProjet.php");
		requete.addParameter("action", "ajouterProject");
		requete.addParameter("titre", titre.getValueAsString());
		requete.addParameter("idBranche", "1");
		requete.addParameter("synopsis", synopsis.getText());
		requete.addParameter("responsables", responsable.getText());
		requete.addParameter("auteurs", auteurs.getText());
		requete.addParameter("motsCle", motsCles.getText());
		try {
			requete.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox.alert("Erreur", "Une erreur inconnue s'est produite durant l'enregistrement.");
				}

				public void onResponseReceived(Request request, Response response) {
					MessageBox.alert(response.getText());				
				}
			});
		} catch (RequestException e1) {
			MessageBox.alert("Erreur", "Une erreur inconnue s'est produite durant l'enregistrement.");
		}
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
					datas.loadXmlData(response.getText(), false);
					for(int i=0; i<datas.getCount();i++)
						listeCours.addItem(datas.getAt(i).getAsString("nom"));					
				}
			});
		} catch (RequestException e1) {
			MessageBox.alert("Erreur", "Une erreur inconnue s'est produite durant la récupération des cours.");
		}
	}
	
}
