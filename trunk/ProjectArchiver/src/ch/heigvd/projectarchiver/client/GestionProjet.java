package ch.heigvd.projectarchiver.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.data.XmlReader;

public class GestionProjet extends VerticalPanel {
	
	/*final private XmlReader reader = new XmlReader("projets", new RecordDef(  
            new FieldDef[]{  
                    new StringFieldDef("common"),  
                    new StringFieldDef("botanical"),  
                    new StringFieldDef("light"),  
                    new FloatFieldDef("price"),  
                    new DateFieldDef("availDate", "availability", "m/d/Y"),  
                    new BooleanFieldDef("indoor")  
            }  
    ));  */

	
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
