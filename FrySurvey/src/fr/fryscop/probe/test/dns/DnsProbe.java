package fr.fryscop.probe.test.dns;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.Probe;
import fr.fryscop.probe.ProbeType;
import fr.fryscop.probe.configuration.log.ProbeLogger;
import fr.fryscop.probe.test.ProbeTest;

/*
 * DNS --> 20 sondes proches des resolvers / every minutes 
 *   DNS service availability <--- pas d'erreur sur un dig sur un ndd de la zone si non d�but du compteur jusqu'� disparition erreur 
 *   DNS name server availability <--- m�me principe que pr�c�dent mais sur les serveur frontaux 
 *   TCP DNS resolution RTT <--- mesure du temps de la requete DNS en TCP = temps "query time" fournit par dig, + ajouter option "+tcp" 
 *   UDP DNS resolution RTT <--- mesure du temps de la requete DNS en UDP = temps "query time" fournit par dig 
 *   DNS update time <--- temps entre acquittement d'une modification EPP et la prise en compte effective dans le dns (valider les donn�es visible dans le dns � modifier) 
 *  ==> si RTT 5 fois sup�rieur au temps SLR alors service unavailable pour la sonde
 */
public class DnsProbe extends Probe implements ProbeTest {

	private static final Logger	logger	= LoggerFactory.getLogger(DnsProbe.class);

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

	void dnsServiceAvailability() {

	}

	void dnsNameServerAvailability() throws IOException {
		/* dig @g.ext.nic.fr afnic.fr ANY +short +tcp */
		String argDig[] = { "@g.ext.nic.fr", "afnic.fr", "ANY", "+short", "+tcp" };
		long requestTime = Dig.request(argDig);
		System.out.println("dnsNameServerAvailability - requestTime:" + requestTime);

	}

	public static void main(String arg[]) throws IOException {
		DnsProbe probe = new DnsProbe();
		probe.setName("afnic");
		probe.setTld("fr");
		probe.setType(ProbeType.Dns);
		
		probe.dnsNameServerAvailability();
	}

}
