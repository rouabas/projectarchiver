package ch.heigvd.projectarchiver.client;

import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

public class ListeDeProjets extends GridPanel{
	
	// Description des colonnes de la liste
	private final static ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{  
            new ColumnConfig("titre", "titre", 100, true),
            new ColumnConfig("branche", "branche", 80, true),  
            new ColumnConfig("synopsis", "synopsis", 110, true),
            new ColumnConfig("ajouteLe", "ajouteLe", 80, true),
            new ColumnConfig("responsables", "responsables", 150, true),
            new ColumnConfig("auteurs", "auteurs", 150, true),
            new ColumnConfig("motscle", "motscle", 150, true),
            new ColumnConfig("nomArchive", "nomArchive", 80, true),
    });

	/**
	 * Constructeur
	 * @param store Le store qui contient les données
	 * @param titre Le titre 
	 */
	public ListeDeProjets(Store store, String titre) {
		super(store, columnModel);
		setTitle(titre);
		setWidth("900px");
		setHeight("400px");
	}
}
