<?php


//////////////////////////////////////////////////////////////////
// Fichier: authentification.php
// But    : Fonctions pour gérer l'authentification
// Date   : 18 mai 2009
//////////////////////////////////////////////////////////////////

include "LDAP.php";

//////////////////////////////////////////////////////////////////
// But         : Authentifier un utilisateur
// $login      : Le login de l'utilisateur
// $motDePasse : Le mot de passe de l'utilisateur
// Résultat    : Renvoie le nom complet de l'utilisateur,
//			   : '!false' sinon
// Erreur      : '!erreurBD' => problème de connexion à la BD
//             : '!erreurLDAP' => problème avec le LDAP
//////////////////////////////////////////////////////////////////
function authentification($login, $motDePasse) {

	// Comme les erreurs LDAP s'affichent via des warnings, on les supprime
	error_reporting(0);

	// Comme le ldap permet une authentification anonyme, on test
	// qu'on reçoive effectivement un login et un mdp
	if ($login == "" or $motDePasse == "")
		return "!false";

	// Connexion au ldap
	$connexionLDAP = ldap_connect(ADRESSE_LDAP, PORT_LDAP);

	// Spécification du protocole utilisé
	ldap_set_option($connexionLDAP, LDAP_OPT_PROTOCOL_VERSION, 3);

	// Authentification
	ldap_bind($connexionLDAP, $login, $motDePasse);

	// Vérification de la dernière erreur LDAP (prob de connexion ou mauvais login)
	// Login OK
	if (ldap_errno($connexionLDAP) == 0) {
		// On vérifie s'il s'agit d'un professeur 
		$filtre = "userPrincipalName=$login";
		$champs = array (
			"givenname",
			"sn"
		);
		$recherche = ldap_search($connexionLDAP, LDAP_DN, $filtre, $champs);
		$resultat = ldap_get_entries($connexionLDAP, $recherche);
		ldap_close($connexionLDAP);
		
		// Si on a trouvé une entrée, c'est bon
		if ($resultat["count"] == 1) {
			$_SESSION["login"] = $login;
			return $resultat[0]["givenname"][0] . " " . $resultat[0]["sn"][0];
		}
		else
		{
			return "!false";
		}
	}
	// Mauvais login
	else
		if (ldap_errno($connexionLDAP) == LDAP_MAUVAIS_LOGIN)
			return "!false";
	// Autre erreur
	else
		return "!erreurLDAP";

}

//////////////////////////////////////////////////////////////////
// But      : Se déconnecter d'une session
// Résultat : Aucun résultat!
//////////////////////////////////////////////////////////////////
function seDeconnecter() {
	session_start(); // on démarre la session
	session_unset(); // on efface toutes les variables de session
	session_destroy(); // on detruit la session en cours.
	return "";
}

//////////////////////////////////////////////////////////////////
// But      : Savoir si un utilisateur est administrateur
// $login   : Le login de l'utilisateur
// Résultat : Renvoie 'true' si l'utilisateur est connecte,
//          : 'false' sinon
//////////////////////////////////////////////////////////////////
function estConnecte() {
	include 'Session.php';

	// On vérifie qu'une session est ouverte 
	if ($estLogue)
		return "true";
	else
		return "false";
}

//////////////////////////////////////////////////////////////////
// But: Traiter les requêtes
//////////////////////////////////////////////////////////////////

header('Content-Type: text/plain; charset=utf-8');

switch ($_POST['action']) {

	case "authentification" :
		echo authentification($_POST['login'], $_POST['motDePasse']);
		break;

	case "seDeconnecter" :
		echo seDeconnecter();
		break;

	case "estConnecte" :
		echo estConnecte();
		break;
}
?>