package ch.heigvd.projectarchiver.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

public class AjoutCours extends Panel{
	
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
		// le flux XML
		XmlReader xml = new XmlReader("cours", new RecordDef(
				new FieldDef[]{
						new StringFieldDef("nom")
				}
		));
		
		// le conteneur pour nos données
		Store datas = new Store(xml);
		datas.loadXmlData(getXml(), true);
		
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
		grille.setStyleName("grille");
		return grille;
	}
	
	private String getXml(){
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<listeDesCours>");
		xml.append("<cours>");
		xml.append("<id>1</id>");
		xml.append("<nom>test</nom>");
		xml.append("</cours>");
		
		xml.append("<cours>");
		xml.append("<id>2</id>");
		xml.append("<nom>aaa</nom>");
		xml.append("</cours>");
		
		xml.append("<cours>");
		xml.append("<id>3</id>");
		xml.append("<nom>www</nom>");
		xml.append("</cours>");	
		
		xml.append("</listeDesCours>");
		return xml.toString();
	}
	
	private HorizontalPanel getChamps(){
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("210");
		TextField cours = new TextField();
		cours.setStyleName("textbouton");
		Button ok = new Button("ok");
		panel.add(cours);
		panel.add(ok);
		return panel;
	}
}
