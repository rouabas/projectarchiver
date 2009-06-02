package ch.heigvd.projectarchiver.client;

import ch.heigvd.projectarchiver.client.utils.AjaxRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

public abstract class ModeleEditionProjet extends VerticalPanel {
	
	private final int LARGEURTEXTAREA = 300;
	
	// Définition du champ contenu (pour se simplifier la vie, on utilise la même définition
	// de champ pour les trois grilles responsables, auteurs, mots-clés
	private final RecordDef champContenu = new RecordDef(new FieldDef[]{new StringFieldDef("contenu")});
	
	// Ce qui contiendra les données de grilles responsables, auteurs et mots-clés
	private final Store storeResponsables = new Store(champContenu);
	private final Store storeAuteurs = new Store(champContenu);
	private final Store storeMotsCles = new Store(champContenu);
	
	// Les définitions des colonnes de grilles responsables et auteurs
	private final ColumnModel columnModelPersonnes = new ColumnModel(new ColumnConfig[]{
			new ColumnConfig("Nom", "contenu", LARGEURTEXTAREA-60, true),
			new ColumnConfig("Suppimer", "supprimer", 30)
			});
	
	// La définition de la colonne de la grille mots-clés
	private final ColumnModel columnModelMotsCles = new ColumnModel(new ColumnConfig[]{
			new ColumnConfig("Mot-clé", "contenu", LARGEURTEXTAREA-60, true),
			new ColumnConfig("Suppimer", "supprimer", 30)
			});
	
	// Les champs qui représentes un fichier
	private final GridPanel grilleResponsables = new GridPanel(storeResponsables, columnModelPersonnes);
	private final GridPanel grilleAuteurs = new GridPanel(storeAuteurs, columnModelPersonnes);
	private final GridPanel grilleMotsCles = new GridPanel(storeMotsCles, columnModelMotsCles);
	private final TextField titre = new TextField();
	private final TextArea synopsis = new TextArea();
	// Servira à remplir les grilles
	private final TextField txtAjoutResp = new TextField();
	private final TextField txtAjoutAuth = new TextField();
	private final TextField txtAjoutMC = new TextField();
	
	private final String SEPARATEUR = ";";
	
	// le flux XML pour récupérer la liste des cours
	private final XmlReader xmlCours = new XmlReader("cours", new RecordDef(
			new FieldDef[] { new StringFieldDef("nom"), new StringFieldDef("@id") }));
	// le conteneur pour nos données pour la liste des cours
	private final Store donnéesCours = new Store(xmlCours);
	private ListBox listeCours = new ListBox();
	
	// Contiendra le formulaire
	FlexTable formulaire = new FlexTable();
	
	public ModeleEditionProjet(String titrePage) {
		setWidth("100%");
		setStyleName("margeSup20");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		Panel panelProjet = new Panel();
		panelProjet.setBorder(true);
		panelProjet.setPaddings(15);
		panelProjet.setTitle(titrePage);
		panelProjet.setWidth(500);
		panelProjet.setShadow(true);

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

		// Récupère la liste des cours
		getCoursEnXml();

		formulaire.setWidget(0, 1, listeCours);
		titre.setWidth(LARGEURTEXTAREA);
		formulaire.setWidget(1, 1, titre);

		grilleResponsables.setHeight("100px");
		grilleResponsables.setWidth(String.valueOf(LARGEURTEXTAREA));
		grilleResponsables.setHideColumnHeader(true);
		// Pour pouvoir supprimer un élément de la grille
		grilleResponsables.getColumnModel().setRenderer(1, new ColonneSupprimer());
		grilleResponsables.addGridCellListener(new ListenerSupprimer());
		VerticalPanel panelResponsables = new VerticalPanel();
		panelResponsables.add(grilleResponsables);
		HorizontalPanel panelRespTxtBtn = new HorizontalPanel();
		Button btnAjoutResp = new Button("+");
		// Action du bouton de ok
		btnAjoutResp.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (txtAjoutResp.getText() != "")
					ajouterElement(txtAjoutResp.getText(), storeResponsables);
				txtAjoutResp.setValue("");
				txtAjoutResp.focus();
			}
		});
		txtAjoutResp.addListener(new ListenerAjouterTexte(storeResponsables));
		panelRespTxtBtn.add(txtAjoutResp);
		panelRespTxtBtn.add(btnAjoutResp);
		panelResponsables.add(panelRespTxtBtn);
		formulaire.setWidget(2, 1, panelResponsables);

		grilleAuteurs.setHeight("100px");
		grilleAuteurs.setWidth(String.valueOf(LARGEURTEXTAREA));
		grilleAuteurs.setHideColumnHeader(true);
		// Pour pouvoir supprimer un élément de la grille
		grilleAuteurs.getColumnModel().setRenderer(1, new ColonneSupprimer());
		grilleAuteurs.addGridCellListener(new ListenerSupprimer());
		VerticalPanel panelAuteurs = new VerticalPanel();
		panelAuteurs.add(grilleAuteurs);
		HorizontalPanel panelAuthTxtBtn = new HorizontalPanel();
		Button btnAjoutAuth = new Button("+");
		// Action du bouton de ok
		btnAjoutAuth.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (txtAjoutAuth.getText() != "")
					ajouterElement(txtAjoutAuth.getText(), storeAuteurs);
				txtAjoutAuth.setValue("");
				txtAjoutAuth.focus();
			}
		});
		txtAjoutAuth.addListener(new ListenerAjouterTexte(storeAuteurs));
		panelAuthTxtBtn.add(txtAjoutAuth);
		panelAuthTxtBtn.add(btnAjoutAuth);
		panelAuteurs.add(panelAuthTxtBtn);
		formulaire.setWidget(3, 1, panelAuteurs);

		grilleMotsCles.setHeight("100px");
		grilleMotsCles.setWidth(String.valueOf(LARGEURTEXTAREA));
		grilleMotsCles.setHideColumnHeader(true);
		grilleMotsCles.addGridCellListener(new ListenerSupprimer());
		// Pour pouvoir supprimer un élément de la grille
		grilleMotsCles.getColumnModel().setRenderer(1, new ColonneSupprimer());
		VerticalPanel panelMotCles = new VerticalPanel();
		panelMotCles.add(grilleMotsCles);
		HorizontalPanel panelMCTxtBtn = new HorizontalPanel();
		Button btnAjoutMC = new Button("+");
		// Action du bouton de ok
		btnAjoutMC.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (txtAjoutMC.getText() != "")
					ajouterElement(txtAjoutMC.getText(), storeMotsCles);
				txtAjoutMC.setValue("");
				txtAjoutMC.focus();
			}
		});
		txtAjoutMC.addListener(new ListenerAjouterTexte(storeMotsCles));
		panelMCTxtBtn.add(txtAjoutMC);
		panelMCTxtBtn.add(btnAjoutMC);
		panelMotCles.add(panelMCTxtBtn);
		formulaire.setWidget(4, 1, panelMotCles);

		synopsis.setHeight("200");
		synopsis.setWidth(String.valueOf(LARGEURTEXTAREA));
		formulaire.setWidget(5, 1, synopsis);

		panelProjet.add(formulaire);
		add(panelProjet);
	}
	

	/**
	 * Insère le formulaire pour créer/modifier le projet
	 * @param form Le formulaire
	 */
	public void setFormulaire (FormPanel form) {
		formulaire.setWidget(6, 1, form);
	}

	/**
	 * @return Le titre du projet
	 */
	public String getTitre () {
		return titre.getText();
	}
	
	/**
	 * @return La branche choisie
	 */
	public String getBranche () {
		int index = donnéesCours.find("nom", listeCours.getItemText(listeCours.getSelectedIndex()), 0, false, true);
		// Si l'index est négatif, c'est bizarre!
		if (index != -1)
			return donnéesCours.getAt(index).getAsString("@id");
		else
			return "0";
	}
	
	/**
	 * @return Les responsables du projet
	 */
	public String getResponsables () {
		// Préparation du champ responsables
		String responsables = "";
		for (int i = 0; i < storeResponsables.getCount(); i++) {
			responsables += storeResponsables.getAt(i).getAsString("contenu");
			// Ajout d'un séparateur
			if (i < storeResponsables.getCount()-1)
				responsables += SEPARATEUR;
		}
		return responsables;
	}

	/**
	 * @return Les auteurs du projet
	 */
	public String getAuteurs () {
		// Préparation du champ auteur
		String auteurs = "";
		for (int i = 0; i < storeAuteurs.getCount(); i++) {
			auteurs += storeAuteurs.getAt(i).getAsString("contenu");
			// Ajout d'un séparateur
			if (i < storeAuteurs.getCount()-1)
				auteurs += SEPARATEUR;
		}
		return auteurs;
	}
	
	/**
	 * @return Les mots clés du projet
	 */
	public String getMotsCles () {
		// Préparation du champ mots-clés
		String motsCles = "";
		for (int i = 0; i < storeMotsCles.getCount(); i++) {
			motsCles += storeMotsCles.getAt(i).getAsString("contenu");
			// Ajout d'un séparateur
			if (i < storeMotsCles.getCount()-1)
				motsCles += SEPARATEUR;
		}
		return motsCles;
	}
	
	/**
	 * @return Le synopsis du projet
	 */
	public String getSynopsis () {
		return synopsis.getText();
	}
		
	/**
	 * Récupération de la liste des cours
	 */
	private void getCoursEnXml() {
		AjaxRequest requete = new AjaxRequest("php/cours.php");
		requete.addParameter("action", "coursXML");
		try {
			requete.send(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					MessageBox
							.alert("Une erreur inconnue s'est produite durant la récupération des cours.");
				}

				public void onResponseReceived(Request request,
						Response response) {
					donnéesCours.loadXmlData(response.getText(), false);
					for (int i = 0; i < donnéesCours.getCount(); i++)
						listeCours.addItem(donnéesCours.getAt(i).getAsString("nom"));
				}
			});
		} catch (RequestException e1) {
			MessageBox
					.alert("Erreur",
							"Une erreur inconnue s'est produite durant la récupération des cours.");
		}
	}
	
	/**
	 * Classe interne pour représenter la colonne qui contient les boutons pour
	 * supprimer un élément d'une grille
	 */
	private class ColonneSupprimer implements Renderer{

		public String render(Object value, CellMetadata cellMetadata,
				Record record, int rowIndex, int colNum, Store store) {
			return "<img src='" + GWT.getModuleBaseURL() + "../images/x.gif' alt='Voir' style='cursor:pointer' />";
		}
	}
	
	/**
	 * Classe interne pour représenter l'action d'un clic sur un bouton pour supprimer un élément
	 * d'une grille
	 */
	private class ListenerSupprimer extends GridCellListenerAdapter {
		
		@Override
		public void onCellClick(GridPanel grid, int rowIndex, int colIndex,
				EventObject e) {
			if (colIndex == 1)
				grid.getStore().remove(grid.getSelectionModel().getSelected());
		}
	}
	
	/**
	 * Classe interne pour pouvoir valider avec return les saisies dans les grilles
	 */
	private class ListenerAjouterTexte extends TextFieldListenerAdapter {

		private Store store; // Le store dans lequel on désire ajouter un élément
		
		public ListenerAjouterTexte (Store store) {
			this.store = store;
		}
		
		@Override
		public void onSpecialKey(Field field, EventObject e) {
			if (e.getKey() == KeyCodes.KEY_ENTER && !field.getValueAsString().equals("")) {
				ajouterElement(field.getValueAsString(), store);
				field.setValue("");
				field.focus();
			}
		}
	}
	
	/**
	 * Ajoute un élément dans une grille
	 * @param element L'élément sous forme de string
	 * @param store Le store de la grille
	 */
	private void ajouterElement(String element, Store store) {
		Record nouvelElement = champContenu.createRecord(new String[]{element});
		store.add(nouvelElement);
	}
	
	/**
	 * Charge les responsables dans le champ responsables
	 * @param responsables Les responsables séparés par des ","
	 */
	public void chargerResponsables (String responsables) {
		String[] tabResponsables = responsables.split(",");
		Record[] elements = new Record[tabResponsables.length];
		for (int i = 0; i < tabResponsables.length; i++) {
			elements[i] = champContenu.createRecord(new String[]{tabResponsables[i]});
		}
		storeResponsables.add(elements);
	}
	
	/**
	 * Charge les auteurs dans le champ auteurs
	 * @param auteurs Les auteurs séparés par des ","
	 */
	public void chargerAuteurs (String auteurs) {
		String[] tabAuteurs = auteurs.split(",");
		Record[] elements = new Record[tabAuteurs.length];
		for (int i = 0; i < tabAuteurs.length; i++) {
			elements[i] = champContenu.createRecord(new String[]{tabAuteurs[i]});
		}	
		storeAuteurs.add(elements);
	}
	
	/**
	 * Charge les mots clés dans le champ mots clés
	 * @param auteurs Les mots clés séparés par des ","
	 */
	public void chargerMotsCles (String motscles) {
		String[] tabMotscles = motscles.split(",");
		Record[] elements = new Record[tabMotscles.length];
		for (int i = 0; i < tabMotscles.length; i++) {
			elements[i] = champContenu.createRecord(new String[]{tabMotscles[i]});
		}	
		storeMotsCles.add(elements);
	}
	
	/**
	 * Charge le titre
	 * @param titre Le titre
	 */
	public void chargerTitre (String titre) {
		this.titre.setValue(titre);
	}
	
	/**
	 * Charge le synopsis
	 * @param synopsis Le synopsis
	 */
	public void chargerSynopsis (String synopsis) {
		this.synopsis.setValue(synopsis);
	}
	
	/**
	 * Charge le cours
	 * @param cours Le nom du cours
	 */
	public void chargerCours (String cours) {
		// On recherche la branche
		for (int i = 0; i < listeCours.getItemCount(); i++)
			if (listeCours.getItemText(i).equals(cours)) {
				listeCours.setSelectedIndex(i);
				break;
			}
	}
}
