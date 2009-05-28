package ch.heigvd.projectarchiver.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;

public class GestionProjet extends VerticalPanel {
	
	final private XmlReader reader = new XmlReader("projets", new RecordDef(  
            new FieldDef[]{  
            		new StringFieldDef("titre"),
					new StringFieldDef("idBranche"),
					new StringFieldDef("synopsis"),
					new StringFieldDef("responsable"),
					new StringFieldDef("synopsis"),
					new StringFieldDef("ajoute_le"),
            }  
    ));

	
	/**
	 * Constructeur
	 */
	public GestionProjet () {
		setWidth("900px");
		setStyleName("margeSup20");
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		//add(new ListeDeProjets());
	}
}
