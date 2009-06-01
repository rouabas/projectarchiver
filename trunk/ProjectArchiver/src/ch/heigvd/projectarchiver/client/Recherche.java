package ch.heigvd.projectarchiver.client;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;

public class Recherche extends Panel{
	
	
	private final TextField filtre = new TextField();
	private final Button btnRechercher = new Button("Rechercher");
	private final Panel resultats = new Panel();

	private void rechercher(String string) {
		
		resultats.add(new Hyperlink("Les pucerons","Lespucerons/index"));
		
	}
	
	public Recherche(){
		setBorder(true);
		setPaddings(15);
		setTitle("Recherche de projets");
		setWidth(300);
		setShadow(true);
		setCollapsible(true);
		setWidth("100%");
		setHeight("1000px");
				
		// Ajoute les composants au formulaire
		HorizontalPanel controles = new HorizontalPanel();
		controles.add(filtre);
		controles.add(btnRechercher);

		rechercher("");
		
		add(controles);
		add(resultats);
		
		btnRechercher.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				rechercher(filtre.toString());
			}
		});		
	}	
}
