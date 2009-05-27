package ch.heigvd.projectarchiver.client;

import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

public class ListeDeProjets extends GridPanel{
	
	// Description des colonnes de la liste
	private final static ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{  
            new ColumnConfig("titre", "titre", 60, true),
            new ColumnConfig("ajoute_le", "ajoute_le", 60, true),  
            new ColumnConfig("auteurs", "auteurs", 80, true),
            new ColumnConfig("motscle", "motscle", 345, true),
            new ColumnConfig("responsables", "responsables", 345, true)
    });

	/**
	 * Constructeur
	 * @param store Le store qui contient les données
	 * @param titre Le titre 
	 */
	public ListeDeProjets(Store store, String titre) {
		super(store, columnModel);
		setTitle(titre);
	}
}
