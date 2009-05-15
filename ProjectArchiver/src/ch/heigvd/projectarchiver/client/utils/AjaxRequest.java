/**
 * Fichier : AjaxRequest.java
 * Auteur  : Grégory Moinat
 * Date    : 9 mai 2009
 */

package ch.heigvd.projectarchiver.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;

/**
 * But: Faciliter la création et l'envoi de requête AJAX
 */
public class AjaxRequest {
	private StringBuffer postData; // Utilisé pour les paramètres de la requête
	private RequestBuilder request; // La requête
	
	/**
	 * Constructeur
	 * @param url L'url de la requête
	 */
	public AjaxRequest (String url) {
		postData = new StringBuffer();
		request = new RequestBuilder(RequestBuilder.POST, GWT.getModuleBaseURL() + "../" + url);
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
	}
	
	/**
	 * Ajoute un paramètre à la requête
	 * @param name Nom du paramètre
	 * @param value Valeur du paramètre
	 */
	public void addParameter (String name, String value) {
		postData.append(URL.encode(name)).append("=").append(URL.encode(value));
		postData.append("&");
	}
	
	/**
	 * Envoie la requête
	 * @param callback Callback pour gérer la réponse
	 * @throws RequestException
	 */
	public void send (RequestCallback callback) throws RequestException {
		request.sendRequest(postData.toString(), callback);
	}
	
	
}
