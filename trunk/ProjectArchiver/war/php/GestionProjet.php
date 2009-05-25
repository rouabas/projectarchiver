<?php

/**
 * Fichier: projectManagement.php
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
 * @return "!ok" si tout s'est bien passé, "!error" sinon
 */
function ajouterProject ($titre, $idBranche, $synopsis, $responsables, $auteurs, $motsCle) {
		error_reporting(0);
		set_error_handler("traitementErreurs");
	
		$document = new DOMDocument();
		// Supprime l'indentation avant la lecture
		$document->preserveWhiteSpace = false;
		$document->load("../xml/projets.xml");
		// Réindente le fichier
		$document->formatOutput = true;
		
		// Création de éléments composants le projet
		$nouveauProjet = $document->createElement("projet");
		$nouveauProjet->setAttribute("id", "3"); // TODO Générer un vrai id
		
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
		
		// Récupération de la racine du document pour y ajouter le nouveau projet
		$racine = $document->getElementsByTagName("projets")->item(0);
		$racine->appendChild($nouveauProjet);
		
		// On enregistre le document
		$document->normalizeDocument();
		echo $document->save("../xml/projets.xml");
		
		
		return "!ok";
}

/**
 * Pour traiter les erreurs (DOM ne lève pas des exceptions mais affiche des Warnings)
 */
 function traitementErreurs($errno, $errmsg, $filename, $linenum, $vars) { 
 	echo "!erreur";
 	exit();
 }
 
/**
 * Traitement des requêtes
 */

header('Content-Type: text/plain; charset=utf-8');

switch ($_POST['action']) {

	case "ajouterProject" :
		echo ajouterProject($_POST['titre'], $_POST['idBranche'], $_POST['synopsis'], $_POST['responsables'], $_POST['auteurs'], $_POST['motsCle']);
		break;
}

?>