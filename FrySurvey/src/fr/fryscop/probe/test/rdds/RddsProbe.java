package fr.fryscop.probe.test.rdds;

public class RddsProbe {

	/* 
	 * RDDS --> 10 sondes world wide / toutes les 5 min
	 *  RDDS availability				<--- pas d'erreur lors de requete RDDS si non d�but du compteur jusqu'� disparition erreur
	 *  RDDS query RTT					<--- requete simple sur ndd avec mesure du temps entre l'envoi et la r�ponse
	 *  RDDS update time					<--- temps entre acquittement d'une modification EPP et la prise en compte effective dans rdds
	 * ==> si RTT 5 fois sup�rieur au temps SLR alors service unavailable pour la sonde
	 */
}
