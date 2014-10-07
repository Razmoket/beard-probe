package fr.fryscop.probe.test.dns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.configuration.log.ProbeLogger;
import fr.fryscop.probe.test.ProbeTest;

/*
 * DNS --> 20 sondes proches des resolvers / every minutes DNS service availability <--- pas d'erreur sur un dig sur un ndd de la zone si non
 * d�but du compteur jusqu'� disparition erreur DNS name server availability <--- m�me principe que pr�c�dent mais sur les serveur frontaux TCP
 * DNS resolution RTT <--- mesure du temps de la requete DNS en TCP = temps "query time" fournit par dig, + ajouter option "+tcp" UDP DNS
 * resolution RTT <--- mesure du temps de la requete DNS en UDP = temps "query time" fournit par dig DNS update time <--- temps entre acquittement
 * d'une modification EPP et la prise en compte effective dans le dns (valider les donn�es visible dans le dns � modifier) ==> si RTT 5 fois
 * sup�rieur au temps SLR alors service unavailable pour la sonde
 */
public class DnsProbe implements ProbeTest {

	private static final Logger logger = LoggerFactory.getLogger(DnsProbe.class);
	@Override
    public void launchTest() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public String getTestResult() {
	    // TODO Auto-generated method stub
		
		ProbeLogger.getInstance();
	    return null;
    }

	
	
	
}
