<?php

//////////////////////////////////////////////////////////////////
// Fichier: authentification.php
// But    : Fonctions pour gérer l'authentification
// Date   : 11 décembre 2008
//////////////////////////////////////////////////////////////////

include "LDAP.php";

//////////////////////////////////////////////////////////////////
// But         : Authentifier un utilisateur
// $login      : Le login de l'utilisateur
// $motDePasse : Le mot de passe de l'utilisateur
// Résultat    : Renvoie 'true' si le login existe et que le mot de
//             : passe associé est correct, 'false' sinon.
// Erreur      : '!erreurBD' => problème de connexion à la BD
//             : '!erreurLDAP' => problème avec le LDAP
//////////////////////////////////////////////////////////////////
function authentification ($login, $motDePasse) {
   
   // Comme les erreurs LDAP s'affichent via des warnings, on les supprime
   error_reporting(0);
   
   // Comme le ldap permet une authentification anonyme, on test
   // qu'on reçoive effectivement un login et un mdp
   if ($login == "" or $motDePasse == "")
      return "false";
      
   // Connexion au ldap
   $connexionLDAP = ldap_connect(ADRESSE_LDAP, PORT_LDAP);
      
   // Spécification du protocole utilisé
   ldap_set_option($connexionLDAP, LDAP_OPT_PROTOCOL_VERSION, 3);   
   
   // Authentification
   ldap_bind($connexionLDAP, $login, $motDePasse);
   
   // Vérification de la dernière erreur LDAP (prob de connexion ou mauvais login)
   // Login OK
   if (ldap_errno($connexionLDAP) == 0) {
      
      ldap_close($connexionLDAP);
      
      // Connexion à la base de données
      include 'ConnexionBD.php';
      
      // Vérification de l'existence de l'utilisateur
      $requete = "SELECT COUNT(login) FROM utilisateur WHERE login = '$login'";
      $resultat =  mysql_query($requete);
      $ligne = mysql_fetch_row($resultat);
      
      // On vérifie si on a bien un utilisateur à ce nom
      if ($ligne[0] == 1) {
				session_start();// on démarre une session
				// On enregistre l'id de l'utilisateur dans la base 
				$_SESSION["login"]=$login;
				$_SESSION["motDePasse"]=$motDePasse;
				return "true";
      }
      else
         return "false";
   }
   // Mauvais login
   else if (ldap_errno($connexionLDAP) == LDAP_MAUVAIS_LOGIN)
      return "false";
   // Autre erreur
   else
      return $ERREUR_LDAP;
   
}

//////////////////////////////////////////////////////////////////
// But      : Se déconnecter d'une session
// Résultat : Aucun résultat!
//////////////////////////////////////////////////////////////////
function seDeconnecter() {
   session_start();// on démarre la session
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
function estConnecte () {
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

switch($_POST['action']) {
   
   case "authentification" :   
      echo authentification($_POST['login'],$_POST['motDePasse']);
      break;
   
   case "estAdmin" :
      echo estAdmin($_POST['login']);
      break;
      
   case "login" :
      echo login();
      break;
      
   case "infosUtilisateur" :
      echo infosUtilisateur();
      break;
      
   case "listeVersements" :
      echo listeVersements();
      break;

   case "seDeconnecter" :
      echo seDeconnecter();
      break;
      
   case "estConnecte" :
      echo estConnecte();
      break;
}

?>