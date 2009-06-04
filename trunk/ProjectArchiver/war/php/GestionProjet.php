<?php


/**
 * Fichier: GestionProjet.php
 * But    : Fonctions pour gérer l'authentification
 * Date   : 18 mai 2009
 */

/**
 * But: Ajouter un nouveau projet
 * @param titre Le titre du projet
 * @param idBranche L'id de la branche dans laquelle s'est fait le projet
 * @param synopsis Le résumé du projet
 * @param responsables Les responsables du projet (séparés par des ;)
 * @param auteurs Les auteurs du projets (séparés par des ;)
 * @param motsCle Les mots clés (séparés par des ;)
 * @return "!ok" si tout s'est bien passé, "!error" sinon. Renvoie "!session" si
 * 			l'utilisateur n'a plus de session
 */
function ajouterProjet($id, $titre, $idBranche, $synopsis, $responsables, $auteurs, $motsCle, $nomArchive) {

	// On gère les erreurs nous même (DOM ne lève pas des exceptions mais des Warnings)
	error_reporting(0);
	set_error_handler("traitementErreurs");

	$document = new DOMDocument();
	// Supprime l'indentation avant la lecture
	$document->preserveWhiteSpace = false;
	$document->load("../xml/projets.xml");
	// Réindente le fichier
	$document->formatOutput = true;

	if ($id == "") {
		// Récupération de l'id le plus grand (pour générer l'id de ce projet)
		$xpath = new DOMXPath($document);
		// Récupération des ids des projets
		$ids = $xpath->evaluate("/projets/projet/@id");
		// Sélection du plus grand id
		$id = 0;
		for ($i = 0; $i < $ids->length; $i++)
			if ((int) $ids->item($i)->nodeValue > $id)
				$id = (int) $ids->item($i)->nodeValue;
		// On ajoute 1 pour créer le nouvel id
		$id++;
	}

	// Création de éléments composants le projet
	$nouveauProjet = $document->createElement("projet");
	$nouveauProjet->setAttribute("id", $id);

	// Titre
	$elemTitre = $document->createElement("titre");
	$valeurTitre = $document->createTextNode($titre);
	$elemTitre->appendChild($valeurTitre);
	$nouveauProjet->appendChild($elemTitre);

	// Branche
	$elemIdBranche = $document->createElement("idBranche");
	$valeurIdBranche = $document->createTextNode($idBranche);
	$elemIdBranche->appendChild($valeurIdBranche);
	$nouveauProjet->appendChild($elemIdBranche);

	// Synopsis
	$elemSynopsis = $document->createElement("synopsis");
	$valeurSynopsis = $document->createTextNode($synopsis);
	$elemSynopsis->appendChild($valeurSynopsis);
	$nouveauProjet->appendChild($elemSynopsis);

	// Date
	$date = new DateTime("now", new DateTimeZone("Europe/Berlin"));
	$elemDate = $document->createElement("ajouteLe");
	$valeurDate = $document->createTextNode($date->format("d/m/Y"));
	$elemDate->appendChild($valeurDate);
	$nouveauProjet->appendChild($elemDate);

	// Responsables
	$elemResponsables = $document->createElement("responsables");
	$responsablesSepares = split(";", $responsables);
	for ($i = 0; $i < count($responsablesSepares); $i++) {
		$tabElemResponsable[$i] = $document->createElement("responsable");
		$tabValeursResponsable[$i] = $document->createTextNode($responsablesSepares[$i]);
		$tabElemResponsable[$i]->appendChild($tabValeursResponsable[$i]);
		$elemResponsables->appendChild($tabElemResponsable[$i]);
	}
	$nouveauProjet->appendChild($elemResponsables);

	// Auteurs
	$elemAuteurs = $document->createElement("auteurs");
	$auteursSepares = split(";", $auteurs);
	for ($i = 0; $i < count($auteursSepares); $i++) {
		$tabElemAuteur[$i] = $document->createElement("auteur");
		$tabValeursAuteur[$i] = $document->createTextNode($auteursSepares[$i]);
		$tabElemAuteur[$i]->appendChild($tabValeursAuteur[$i]);
		$elemAuteurs->appendChild($tabElemAuteur[$i]);
	}
	$nouveauProjet->appendChild($elemAuteurs);

	// Mots-clés
	$elemMotsCle = $document->createElement("motsCle");
	$motsCleSepares = split(";", $motsCle);
	for ($i = 0; $i < count($motsCleSepares); $i++) {
		$tabElemMotCle[$i] = $document->createElement("motCle");
		$tabValeursMotCle[$i] = $document->createTextNode($motsCleSepares[$i]);
		$tabElemMotCle[$i]->appendChild($tabValeursMotCle[$i]);
		$elemMotsCle->appendChild($tabElemMotCle[$i]);
	}
	$nouveauProjet->appendChild($elemMotsCle);

	// Nom de l'archive
	$elemArchive = $document->createElement("nomArchive");
	$valeurArchive = $document->createTextNode($nomArchive);
	$elemArchive->appendChild($valeurArchive);
	$nouveauProjet->appendChild($elemArchive);

	// Récupération de la racine du document pour y ajouter le nouveau projet
	$racine = $document->getElementsByTagName("projets")->item(0);
	$racine->appendChild($nouveauProjet);

	// On enregistre le document
	$document->save("../xml/projets.xml");

	return "!ok";
}

/**
 * Pour traiter les erreurs (DOM ne lève pas des exceptions mais affiche des Warnings)
 */
function traitementErreurs($errno, $errmsg, $filename, $linenum, $vars) {
	echo "!erreur $errmsg";
	exit ();
}

/**
 * Liste tous les projets
 */
function listerTousLesProjets() {
	error_reporting(0);
	set_error_handler("traitementErreurs");

	$document = new DOMDocument();
	$document->preserveWhiteSpace = false;
	$document->load("../xml/projets.xml");

	// Récupération de tous les projets avec XPATH
	$xpath = new DOMXPath($document);
	$projets = $xpath->evaluate("/projets/projet");
	return obtenirChaineXMLProjets($projets);
}

/**
 * Filtre tous les projets
 */
function filtrerLesProjets($critere) {
		
	error_reporting(0);
	set_error_handler("traitementErreurs");

	$document = new DOMDocument();
	$document->preserveWhiteSpace = false;
	$document->load("../xml/projets.xml");

	// Récupération de tous les projets avec XPATH
	$xpath = new DOMXPath($document);
												
	$projets = $xpath->evaluate('/projets/projet[contains(., "'. $critere .'")]');
	
	if ($projets->length == 0)
		return "!aucun";
									
	//return $projets->length;
	return obtenirChaineXMLProjets($projets);
}

/**
 * Convertit un noeud obtenu avec xpath en chaine de texte XML prête à l'emploi
 * @param noeudProjets Le noeud obtenu avec xpath
 */
function obtenirChaineXMLProjets($noeudProjets) {
	// On écrit chaque projet
	$chaineXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
	$chaineXML .= "<projets>";
	for ($i = 0; $i < $noeudProjets->length; $i++) {
		$projetCourant = $noeudProjets->item($i);
		$elementsDuProjet = $projetCourant->childNodes;
		$chaineXML .= "<projet id='" . $projetCourant->attributes->getNamedItem("id")->nodeValue . "'>\n";
		// On parcours chaque élément du projet
		for ($j = 0; $j < $elementsDuProjet->length; $j++) {
			$elementCourant = $elementsDuProjet->item($j);
			// On traite les cas particuliers	
			if ($elementCourant->localName == "auteurs") {
				$auteurs = $elementCourant->childNodes;
				$chaineXML .= "<auteurs>";
				for ($auteur = 0; $auteur < $auteurs->length; $auteur++) {
					// On rassemble tous les auteurs dans une balise
					$chaineXML .= $auteurs->item($auteur)->nodeValue;
					// Séparation par des virgules et des espaces
					if ($auteur < $auteurs->length - 1)
						$chaineXML .= ", ";
				}
				$chaineXML .= "</auteurs>\n";
			} else
				if ($elementCourant->localName == "idBranche") {
					$branchesDocument = new DOMDocument();
					$branchesDocument->load("../xml/listeDesCours.xml");
					$xpathBranche = new DOMXPath($branchesDocument);
					$resultat = $xpathBranche->evaluate("//cours[@id = '" . $elementCourant->nodeValue . "']");
					if ($resultat->length > 0) {
						$nomBranche = $resultat->item(0);
						$chaineXML .= "<branche>" . $nomBranche->nodeValue . "</branche>";
					}

				} else
					if ($elementCourant->localName == "responsables") {
						$responsables = $elementCourant->childNodes;
						$chaineXML .= "<responsables>";
						for ($responsable = 0; $responsable < $responsables->length; $responsable++) {
							// On rassemble tous les responsables dans une balise
							$chaineXML .= $responsables->item($responsable)->nodeValue;
							// Séparation par des virgules et des espaces
							if ($responsable < $responsables->length - 1)
								$chaineXML .= ", ";
						}
						$chaineXML .= "</responsables>\n";
					} else
						if ($elementCourant->localName == "motsCle") {
							$motsCle = $elementCourant->childNodes;
							$chaineXML .= "<motsCle>";
							for ($motCle = 0; $motCle < $motsCle->length; $motCle++) {
								// On rassemble tous les responsables dans une balise
								$chaineXML .= $motsCle->item($motCle)->nodeValue;
								// Séparation par des virgules et des espaces
								if ($motCle < $motsCle->length - 1)
									$chaineXML .= ", ";
							}
							$chaineXML .= "</motsCle>\n";
						} else {
							$chaineXML .= "<" . $elementCourant->localName . ">";
							$chaineXML .= $elementCourant->nodeValue;
							$chaineXML .= "</" . $elementCourant->localName . ">\n";
						}
		}
		$chaineXML .= "</projet>\n";

	}

	$chaineXML .= "</projets>\n";

	return $chaineXML;

}

/**
 * Fonction pour supprimer un projet en fonction de son id
 * retourne !ok si le projet a bien été supprimé et !not found
 * si le projet n'a pas été trouvé
 * @param id L'id du projet
 * @param aussiArchive Vrai s'il faut aussi supprimer l'archive
 */
function supprimerProjet($id, $aussiArchive) {

	// On gère les erreurs nous même (DOM ne lève pas des exceptions mais des Warnings)
	error_reporting(0);
	set_error_handler("traitementErreurs");
	
	// Suppression éventuelle de l'archive
	if ($aussiArchive) {
		if (!unlink("../projectFiles/$id/". getNomArchive($id)))
			return "!error";
		if (!rmdir("../projectFiles/$id/"))
			return "!error";
	}

	$document = new DOMDocument();
	// Supprime l'indentation avant la lecture
	$document->preserveWhiteSpace = false;
	$document->load("../xml/projets.xml");
	// Réindente le fichier
	$document->formatOutput = true;

	$projets = $document->documentElement;

	// Récupération du projet ayant l'id concerne
	$xpath = new DOMXPath($document);
	$projet = $xpath->query("/projets/projet[@id='" . $id . "']");
	$projetSupprime = null;
	foreach ($projet as $resultat)
		$projetSupprime = $projets->removeChild($resultat);

	if ($projetSupprime == null)
		return "!not found";

	// On enregistre le document
	$document->save("../xml/projets.xml");

	return "!ok";
}

/**
 * retourne le nom de l'archive du projet donné par son id
 */
function getNomArchive($id) {
	// On gère les erreurs nous même (DOM ne lève pas des exceptions mais des Warnings)
	error_reporting(0);
	set_error_handler("traitementErreurs");

	$document = new DOMDocument();
	// Supprime l'indentation avant la lecture
	$document->preserveWhiteSpace = false;
	$document->load("../xml/projets.xml");
	// Réindente le fichier
	$document->formatOutput = true;

	// Récupération du projet ayant l'id concerne
	$xpath = new DOMXPath($document);
	$noms = $xpath->query("/projets/projet[@id='" . $id . "']/nomArchive");

	foreach ($noms as $nom)
		return $nom->nodeValue;
}

/**
 * Fonction pour modifier un projet donné
 */
function modifierProjet($id, $titre, $idBranche, $synopsis, $responsables, $auteurs, $motsCle, $nomArchive) {

	// On regarde si l'archive a été modifiée
	if ($nomArchive == "") {
		$nom = getNomArchive($id);
		supprimerProjet($id, false);
		ajouterProjet($id, $titre, $idBranche, $synopsis, $responsables, $auteurs, $motsCle, $nom);
	} else {
		// On supprime l'ancien fichier
		if (!unlink("../projectFiles/$id/". getNomArchive($id)))
			return "!error";
		supprimerProjet($id, false);
		ajouterProjet($id, $titre, $idBranche, $synopsis, $responsables, $auteurs, $motsCle, $nomArchive);
	}

	return "!ok";
}

/**
 * Permet d'uploader un fichier de projet
 * @param l'id du projet
 * @return !ok si l'upload a réussi'
 */
function uploadFichier($id) {

	// Si l'id n'est pas précisé, on en génère un
	if ($id == "") {
		$document = new DOMDocument();
		// Supprime l'indentation avant la lecture
		$document->preserveWhiteSpace = false;
		$document->load("../xml/projets.xml");
		$xpath = new DOMXPath($document);
		// Récupération des ids des projets
		$ids = $xpath->evaluate("/projets/projet/@id");
		// Sélection du plus grand id
		$idGenere = 0;
		for ($i = 0; $i < $ids->length; $i++)
			if ((int) $ids->item($i)->nodeValue > $id)
				$idGenere = (int) $ids->item($i)->nodeValue;
		// On ajoute 1 pour créer le nouvel id
		$idGenere++;
		// Création du dossier avec l'id généré
		mkdir("../projectFiles/" . $idGenere, 0777, true);
		$emplacementFichier = "../projectFiles/" . $idGenere . "/" . $_FILES['fichier']['name'];
	} else
		$emplacementFichier = "../projectFiles/" . $id . "/" . $_FILES['fichier']['name'];

	move_uploaded_file($_FILES['fichier']['tmp_name'], $emplacementFichier);

	return "!ok";
}

/**
 * Traitement des requêtes
 */

switch ($_POST['action']) {

	case "ajouterProjet" :
		header('Content-Type: text/plain; charset=utf-8');
		// Cette méthode ne doit être accessible par que par un professeur
		include "Session.php";
		if (!$estLogue)
			return "!session";
			
		echo trim(ajouterProjet($_POST['id'], $_POST['titre'], $_POST['idBranche'], $_POST['synopsis'], $_POST['responsables'], $_POST['auteurs'], $_POST['motsCle'], $_POST['nomArchive']));
		break;

	case "listerTousLesProjets" :
		header('Content-Type: text/plain; charset=utf-8');
		echo listerTousLesProjets();
		break;
		
	case "filtrerLesProjets" :
		header('Content-Type: text/plain; charset=utf-8');
		echo filtrerLesProjets($_POST['critere']);
		break;

	case "supprimerProjet" :
		header('Content-Type: text/plain; charset=utf-8');
		// Cette méthode ne doit être accessible par que par un professeur
		include "Session.php";
		if (!$estLogue)
			return "!session";
			
		echo supprimerProjet($_POST['id'], true);
		break;

	case "uploadFichier" :
		// Cette méthode ne doit être accessible par que par un professeur
		include "Session.php";
		if (!$estLogue)
			return "!session";
			
		echo uploadFichier($_POST['id']);
		break;

	case "modifierProjet" :
		header('Content-Type: text/plain; charset=utf-8');
		// Cette méthode ne doit être accessible par que par un professeur
		include "Session.php";
		if (!$estLogue)
			return "!session";
			
		echo modifierProjet($_POST['id'], $_POST['titre'], $_POST['idBranche'], $_POST['synopsis'], $_POST['responsables'], $_POST['auteurs'], $_POST['motsCle'], $_POST['nomArchive']);
		break;		
}
?>