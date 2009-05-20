<?php

//////////////////////////////////////////////////////////////////
// Fichier: projectManagement.php
// But    : Fonctions pour gérer l'authentification
// Date   : 18 mai 2009
//////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////
// But         : Ajouter un nouveau 
// $login      : Le login de l'utilisateur
// $motDePasse : Le mot de passe de l'utilisateur
// Résultat    : Renvoie le nom complet de l'utilisateur,
//			   : '!false' sinon
// Erreur      : '!erreurLDAP' => problème avec le LDAP
//////////////////////////////////////////////////////////////////
function addProject ($titre, $idBranche, $synopsis, $responsables, $auteurs, $motsCle) {
	$newProject = new DOMElement();
	$document = new DOMDocument();
	$document->load("../xml/projets.xml");
	
}

//////////////////////////////////////////////////////////////////
// But: Traiter les requêtes
//////////////////////////////////////////////////////////////////

header('Content-Type: text/plain; charset=utf-8');

switch ($_POST['action']) {

	case "addProject" :
		echo addProject($_POST['titre'], $_POST['idBranche'], $_POST['synopsis'], $_POST['responsables'], $_POST['auteurs'], $_POST['motsCle']);
		break;
}

?>