package ch.heigvd.projectarchiver.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class EditionProjet extends AjoutProjet {
	
private Record infosProjet;
	
	/**
	 * Constructeur
	 * @param infosProjet Les informations sur le projet 
	 */
	public EditionProjet (Record infosProjet) {
		this.infosProjet = infosProjet;
		// TODO: Remplir les champs avec ce qui existe
		// TODO: Supprimer le bouton ajouter
		add(construireBoutons());
	}
	
	
	
	/**
	 * @return Les boutons retour, modifier et supprimer (ces deux derniers n'apparaîssent que
	 * si c'est un professeur qui accède à la page).
	 */
	public HorizontalPanel construireBoutons () {
		HorizontalPanel container = new HorizontalPanel();
		container.setStyleName("margeSup20");
		container.setSpacing(20);
		Button retour = new Button("Retour");
		Button valider = new Button("Valider");
		
		// Action du bouton retour
		retour.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (e.getMouseButton() == 0)
					History.back();
			}
		});
		
		// Action du bouton valider
		valider.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (e.getMouseButton() == 0)
					History.back();
			}
		});
		
		container.add(retour);
		container.add(valider);
		return container;
	}

}
