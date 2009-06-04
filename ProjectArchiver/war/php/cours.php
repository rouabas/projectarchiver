<?php


//////////////////////////////////////////////////////////////////
// Fichier: cours.php
// But    : Fonctions pour gérer les cours
// Date   : 20 mai 2009
//////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////
// But         : Ajouter un cours
// $cours      : Le cours à ajouter
// Résultat    : Renvoie true si l'ajout a fonctionné,
//			   : '!false' sinon
// Erreur      : '!exist' => Le cours existe déjà
//////////////////////////////////////////////////////////////////
function ajouter($cours) {
	
	// On vérifie l'état de la session
	include "Session.php";
	if (!$estLogue)
		return "!session";
	
	// On gère les erreurs nous même (DOM ne lève pas des exceptions mais des Warnings)
	error_reporting(0);
	set_error_handler("traitementErreurs");

	$document = new DOMDocument();
	// Supprime l'indentation avant la lecture
	$document->preserveWhiteSpace = false;
	$document->load("../xml/listeDesCours.xml");
	// Réindente le fichier
	$document->formatOutput = true;
	
	// Récupération de l'id le plus grand (pour générer l'id de ce cours)
	$xpath = new DOMXPath($document);
	// Récupération des ids des projets
	$ids = $xpath->evaluate("/listeDesCours/cours/@id");
	// Sélection du plus grand id
	$id = 0;
	for ($i = 0; $i < $ids->length; $i++)
		if ((int)$ids->item($i)->nodeValue > $id)
			$id = (int)$ids->item($i)->nodeValue;
	// On ajoute 1 pour créer le nouvel id
	$id++;	
	
	// Création de éléments composants le cours
	$nouveauCours = $document->createElement("cours");
	$nouveauCours->setAttribute("id", $id);	
	
	// nom
	$elemNom = $document->createElement("nom");
	$valeurNom = $document->createTextNode($cours);
	$elemNom->appendChild($valeurNom);
	$nouveauCours->appendChild($elemNom);	
	
	// Récupération de la racine du document pour y ajouter le nouveau projet
	$racine = $document->getElementsByTagName("listeDesCours")->item(0);
	$racine->appendChild($nouveauCours);
	
	// On enregistre le document
	$document->normalizeDocument();
	$document->save("../xml/listeDesCours.xml");
	
	return "!ok";	

}

/**
 * Pour traiter les erreurs (DOM ne lève pas des exceptions mais affiche des Warnings)
 */
 function traitementErreurs($errno, $errmsg, $filename, $linenum, $vars) { 
 	echo "!erreur $errmsg";
 	exit();
 }

//////////////////////////////////////////////////////////////////
// But         : Retourne tous les cours au format XML
//////////////////////////////////////////////////////////////////
function coursXML(){
	$file = file_get_contents ("../xml/listeDesCours.xml");
	
	return $file;
}



//////////////////////////////////////////////////////////////////
// But: Traiter les requêtes
//////////////////////////////////////////////////////////////////

header('Content-Type: text/plain; charset=utf-8');

switch ($_POST['action']) {

	case "ajouter" :
		echo ajouter($_POST['cours']);
		break;
		
	case "coursXML" :
		echo coursXML();
		break;

}
?>