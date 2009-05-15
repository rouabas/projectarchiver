<?php
//////////////////////////////////////////////////////////////////
// Fichier: Session.php
// But    : Mise à disposition de la variable est estLogue;
// Date   : 18 décembre 2008
//////////////////////////////////////////////////////////////////

	session_start();// on démarre la session	
	if (!session_is_registered("login"))
		$estLogue = 0 ; // indique que l'on est pas encore logué
	else
		$estLogue = 1 ; // indique que l'on est logué
?> 