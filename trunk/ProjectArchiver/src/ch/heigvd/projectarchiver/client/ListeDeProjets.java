package ch.heigvd.projectarchiver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

public class ListeDeProjets extends GridPanel{
	
	// Description des colonnes de la liste
	private final static ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
			new ColumnConfig("Id", "@id", 0, true),
			new ColumnConfig("", "voir", 30, true),
            new ColumnConfig("Titre", "titre", 100, true),
            new ColumnConfig("Branche", "branche", 100, true),  
            new ColumnConfig("Date d'ajout", "ajouteLe", 100, true),
            new ColumnConfig("Synopsis", "synopsis", 300, true),
            new ColumnConfig("motsCle", "motsCle", 265, true)
    });
	

	/**
	 * Constructeur
	 * @param store Le store qui contient les données
	 * @param titre Le titre 
	 */
	public ListeDeProjets(Store store, String titre) {
		super(store, columnModel);
		setTitle(titre);
		getColumnModel().setHidden(0, true);
		getColumnModel().setRenderer(1, new ViewColumn());
		setTitle(titre);
		setWidth("900px");
		setHeight("400px");
		
		// Action du bouton "voir"
		addGridCellListener(new GridCellListenerAdapter() {

			public void onCellClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
				if (e.getMouseButton() == 0)
					InterfaceProf.getInstance().changerVue(new PageProjet(getStore().getAt(rowIndex)));
			}			
		});
	}

	/**
	 * Classe interne pour représenter la colonne qui contient les boutons pour
	 * afficher la page d'un projet
	 */
	private class ViewColumn implements Renderer{

		public String render(Object value, CellMetadata cellMetadata,
				Record record, int rowIndex, int colNum, Store store) {
			return "<img src='" + GWT.getModuleBaseURL() + "../images/zoom-in.gif' alt='Voir' style='cursor:pointer' />";
		}

	}

}