package fr.fryscop.probe.test.dns;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.network.protocole.dns.ProbeDig;
import fr.fryscop.probe.Probe;
import fr.fryscop.probe.ProbeStatus;
import fr.fryscop.probe.ProbeType;
import fr.fryscop.probe.configuration.log.ProbeLogger;
import fr.fryscop.probe.monitoring.HeartBeat;
import fr.fryscop.probe.test.ProbeTest;
import fr.fryscop.probe.test.dns.parameters.AbstractDnsTestParameters;
import fr.fryscop.probe.test.dns.parameters.DnsNameServerAvailabilityParam;
import fr.fryscop.probe.test.dns.parameters.DnsServiceAvailabilityParam;
import fr.fryscop.probe.test.dns.parameters.TcpDnsResolutionParam;
import fr.fryscop.probe.test.dns.parameters.UdpDnsResolutionParam;

/*
 * DNS --> 20 sondes proches des resolvers / every minutes 
 *   DNS service availability <--- pas d'erreur sur un dig sur un ndd de la zone si non début du compteur jusqu'à disparition erreur 
 *   DNS name server availability <--- même principe que précédent mais sur les serveur frontaux 
 *   TCP DNS resolution RTT <--- mesure du temps de la requete DNS en TCP = temps "query time" fournit par dig, + ajouter option "+tcp" 
 *   UDP DNS resolution RTT <--- mesure du temps de la requete DNS en UDP = temps "query time" fournit par dig 
 *   DNS update time <--- temps entre acquittement d'une modification EPP et la prise en compte effective dans le dns (valider les données visible dans le dns à modifier) 
 *  ==> si RTT 5 fois supérieur au temps SLR alors service unavailable pour la sonde
 */
public class DnsProbe implements ProbeTest {
	private static final Logger logger = LoggerFactory.getLogger(DnsProbe.class);

	private Probe probe;
	private String tld;
	private String digNdd;
	private List<String> serverList;
	private int frequenceTest = 60000; /* 1 minute */

	private static final long TCP_DNS_RTT = 1500;
	private static final long UDP_DNS_RTT = 500;

	DnsServiceAvailabilityParam dnsServiceAvailabilityParam = null;
	List<DnsNameServerAvailabilityParam> dnsNameServerAvailabilityParamList = null;
	TcpDnsResolutionParam tcpDnsResolutionParam = null;
	UdpDnsResolutionParam udpDnsResolutionParam = null;

	public DnsProbe(String name, Probe probe, String tld, String digNdd, List<String> serverList) {
		super();
		this.probe = probe;
		this.tld = tld;
		this.digNdd = digNdd;
		this.serverList = serverList;
		initParam();
	}

	private void initParam() {
		dnsServiceAvailabilityParam = new DnsServiceAvailabilityParam(digNdd);
		tcpDnsResolutionParam = new TcpDnsResolutionParam(digNdd);
		udpDnsResolutionParam = new UdpDnsResolutionParam(digNdd);

		dnsNameServerAvailabilityParamList = new ArrayList<DnsNameServerAvailabilityParam>();
		for (String server : this.serverList) {
			dnsNameServerAvailabilityParamList.add(new DnsNameServerAvailabilityParam(server, digNdd));
		}
	}

	private DnsProbe() {
	}

	@Override
	public void run() {
		while (this.getProbe().getStatus() != ProbeStatus.Stopped) {
			this.launchTest();
			try {
				Thread.sleep(this.frequenceTest);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
				this.getProbe().setStatus(ProbeStatus.Error);
			}
		}
	}

	@Override
	public void launchTest() {

		dnsServiceAvailability();
		if (this.getProbe().getStatus() != ProbeStatus.Unavailable) {
			for (DnsNameServerAvailabilityParam param : this.dnsNameServerAvailabilityParamList) {
				dnsNameServerAvailability(param);
			}
			tcpDnsResolution();
			UdpDnsResolution();
		}
		HeartBeat.sendBeat(this.getProbe());
		getTestResult();

	}

	@Override
	public String getTestResult() {
		if (dnsServiceAvailabilityParam.getUnavaibilityDuration() > 0)
			logger.debug(dnsServiceAvailabilityParam.getTestName() + ".getUnavaibilityDuration(): " + dnsServiceAvailabilityParam.getUnavaibilityDuration());
		if (tcpDnsResolutionParam.getUnavaibilityDuration() > 0)
			logger.debug(tcpDnsResolutionParam.getTestName() + ".getUnavaibilityDuration(): " + tcpDnsResolutionParam.getUnavaibilityDuration());
		if (udpDnsResolutionParam.getUnavaibilityDuration() > 0)
			logger.debug(udpDnsResolutionParam.getTestName() + ".getUnavaibilityDuration(): " + udpDnsResolutionParam.getUnavaibilityDuration());
		for (DnsNameServerAvailabilityParam param : this.dnsNameServerAvailabilityParamList) {
			if (param.getUnavaibilityDuration() > 0)
				logger.debug(param.getTestName() + ".getUnavaibilityDuration(): " + param.getUnavaibilityDuration());
		}
		return null;
	}

	void dnsServiceAvailability() {
		long requestTime = this.dnsTest(dnsServiceAvailabilityParam);
		if (requestTime < 0)
			return;
		ProbeLogger.getLogger().trace(this.probe.toString() + "|" + dnsServiceAvailabilityParam.getTestName() + "|" + requestTime);
	}

	void dnsNameServerAvailability(DnsNameServerAvailabilityParam dnsNameServerAvailabilityParam) {
		/* dig @g.ext.nic.fr afnic.fr ANY +short +tcp */
		long requestTime = this.dnsTest(dnsNameServerAvailabilityParam);
		if (requestTime < 0)
			return;
		ProbeLogger.getLogger().trace(this.probe.toString() + "|" + dnsNameServerAvailabilityParam.getTestName() + "|" + requestTime);
	}

	// TCP DNS resolution RTT
	void tcpDnsResolution() {
		long requestTime = this.dnsTest(tcpDnsResolutionParam);
		if (requestTime < 0)
			return;
		if (requestTime > (TCP_DNS_RTT * 5)) {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + tcpDnsResolutionParam.getTestName() + "|" + ProbeStatus.Unavailable.getDescription() + "|" + requestTime);
		} else if (requestTime > TCP_DNS_RTT) {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + tcpDnsResolutionParam.getTestName() + "|" + ProbeStatus.Ko.getDescription() + "|" + requestTime);
		} else {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + tcpDnsResolutionParam.getTestName() + "|" + requestTime);
		}
	}

	// UDP DNS resolution RTT
	void UdpDnsResolution() {
		long requestTime = this.dnsTest(udpDnsResolutionParam);
		if (requestTime < 0)
			return;
		if (requestTime > (UDP_DNS_RTT * 5)) {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + udpDnsResolutionParam.getTestName() + "|" + ProbeStatus.Unavailable.getDescription() + "|" + requestTime);
		} else if (requestTime > UDP_DNS_RTT) {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + udpDnsResolutionParam.getTestName() + "|" + ProbeStatus.Ko.getDescription() + "|" + requestTime);
		} else {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + udpDnsResolutionParam.getTestName() + "|" + requestTime);
		}
	}

	private long dnsTest(AbstractDnsTestParameters param) {
		long requestTime = -1;
		try {
			requestTime = ProbeDig.request(param.getTestArg(), false);
			param.stopUnavaibility();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + param.getTestName() + "|" + ProbeStatus.Unavailable.getDescription());
			param.startUnavaibility();
		}
		return requestTime;
	}

	public String getName() {
		return this.probe.toString();
	}

	public Probe getProbe() {
		return probe;
	}

	public void setProbe(Probe probe) {
		this.probe = probe;
	}

	public String getTld() {
		return tld;
	}

	public void setTld(String tld) {
		this.tld = tld;
	}

	public String getDigNdd() {
		return digNdd;
	}

	public void setDigNdd(String digNdd) {
		this.digNdd = digNdd;
	}

	public List<String> getServerList() {
		return serverList;
	}

	public void setServerList(List<String> serverList) {
		this.serverList = serverList;
	}

	/* FIXME TEST */

	public static DnsProbe getMockProbe() {
		// init de la sonde
		Probe probe = new Probe();
		probe.setName("test_dns");
		probe.setTld("fr");
		probe.setType(ProbeType.Dns);
		probe.setStatus(ProbeStatus.Ok);

		DnsProbe dnsProbe = new DnsProbe();
		dnsProbe.setDigNdd("afnic.fr");
		dnsProbe.setProbe(probe);

		ArrayList<String> serverList = new ArrayList<String>();
		serverList.add("d.nic.fr");
		serverList.add("f.ext.nic.fr");
		serverList.add("g.ext.nic.fr");
		serverList.add("turlututu.nic.fr");
		dnsProbe.setServerList(serverList);

		dnsProbe.setTld("fr");

		dnsProbe.initParam();

		return dnsProbe;
	}
	/*
	 * public static void main(String arg[]) throws IOException { DnsProbe dnsProbe = getMockProbe();
	 * 
	 * // démarrage sonde Thread probeTest = new Thread(dnsProbe); probeTest.start();
	 * 
	 * // arret de la sonde try { Thread.sleep(600000); } catch (InterruptedException e) { e.printStackTrace(); } dnsProbe.getProbe().stop(); //
	 * dnsProbe.launchTest(); }
	 */
	/* TEST */
}
