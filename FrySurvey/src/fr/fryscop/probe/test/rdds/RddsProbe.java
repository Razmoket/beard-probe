package fr.fryscop.probe.test.rdds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.network.protocole.whois.ProbeWhois;
import fr.fryscop.probe.Probe;
import fr.fryscop.probe.ProbeStatus;
import fr.fryscop.probe.ProbeType;
import fr.fryscop.probe.configuration.log.ProbeLogger;
import fr.fryscop.probe.monitoring.HeartBeat;
import fr.fryscop.probe.test.ProbeTest;
import fr.fryscop.probe.test.rdds.parameters.AbstractRddsTestParameters;
import fr.fryscop.probe.test.rdds.parameters.RddsQueryRttParam;
import fr.fryscop.probe.test.rdds.parameters.RddsServiceAvailabilityParam;

public class RddsProbe implements ProbeTest {

	/*
	 * RDDS --> 10 sondes world wide / toutes les 5 min RDDS availability <--- pas d'erreur lors de requete RDDS si non début du compteur jusqu'à
	 * disparition erreur RDDS query RTT <--- requete simple sur ndd avec mesure du temps entre l'envoi et la réponse RDDS update time <--- temps
	 * entre acquittement d'une modification EPP et la prise en compte effective dans rdds ==> si RTT 5 fois supérieur au temps SLR alors service
	 * unavailable pour la sonde
	 */

	public RddsProbe(Probe probe, String tld, String whoisNdd, String server) {
		super();
		this.probe = probe;
		this.tld = tld;
		this.whoisNdd = whoisNdd;
		this.server = server;
		initParam();
	}

	private RddsProbe() {
	}

	private void initParam() {
		rddsServiceAvailabilityParam = new RddsServiceAvailabilityParam(server, whoisNdd);
		rddsQueryRttParam = new RddsQueryRttParam(server, whoisNdd);
	}

	private static final Logger logger = LoggerFactory.getLogger(RddsProbe.class);

	private Probe probe;
	private String tld;
	private String whoisNdd;
	private String server;
	private int frequenceTest = 300000; /* 5 minutes */

	private static final long RDDS_QUERY_RTT = 2000;

	@Override
	public void launchTest() {
		rddsServiceAvailability();
		if (this.getProbe().getStatus() != ProbeStatus.Unavailable) {
			rddsQueryRtt();
		}
		HeartBeat.sendBeat(this.getProbe());
		getTestResult();
	}

	@Override
	public String getTestResult() {
		if (rddsServiceAvailabilityParam.getUnavaibilityDuration() > 0)
			logger.debug(rddsServiceAvailabilityParam.getTestName() + ".getUnavaibilityDuration(): " + rddsServiceAvailabilityParam.getUnavaibilityDuration());
		if (rddsQueryRttParam.getUnavaibilityDuration() > 0)
			logger.debug(rddsQueryRttParam.getTestName() + ".getUnavaibilityDuration(): " + rddsQueryRttParam.getUnavaibilityDuration());
		return null;
	}

	RddsServiceAvailabilityParam rddsServiceAvailabilityParam = null;

	void rddsServiceAvailability() {
		long requestTime = this.whoisTest(rddsServiceAvailabilityParam);
		if (requestTime < 0)
			return;
		ProbeLogger.getLogger().trace(this.probe.toString() + "|" + rddsServiceAvailabilityParam.getTestName() + "|" + requestTime);
	}

	RddsQueryRttParam rddsQueryRttParam = null;

	void rddsQueryRtt() {
		long requestTime = this.whoisTest(rddsQueryRttParam);
		if (requestTime < 0)
			return;
		if (requestTime > (RDDS_QUERY_RTT * 5)) {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + rddsQueryRttParam.getTestName() + "|" + ProbeStatus.Unavailable.getDescription() + "|" + requestTime);
		} else if (requestTime > RDDS_QUERY_RTT) {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + rddsQueryRttParam.getTestName() + "|" + ProbeStatus.Ko.getDescription() + "|" + requestTime);
		} else {
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + rddsQueryRttParam.getTestName() + "|" + requestTime);
		}
	}

	private long whoisTest(AbstractRddsTestParameters param) {
		long requestTime = -1;
		try {
			requestTime = ProbeWhois.request(param.getTestArg());
			param.stopUnavaibility();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ProbeLogger.getLogger().trace(this.probe.toString() + "|" + param.getTestName() + "|" + ProbeStatus.Unavailable.getDescription());
			param.startUnavaibility();
		}
		return requestTime;
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

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getFrequenceTest() {
		return frequenceTest;
	}

	public void setFrequenceTest(int frequenceTest) {
		this.frequenceTest = frequenceTest;
	}

	public String getWhoisNdd() {
		return whoisNdd;
	}

	public void setWhoisNdd(String whoisNdd) {
		this.whoisNdd = whoisNdd;
	}

	public String getName() {
		return this.probe.toString();
	}

	// FIXME TEST
	public static void main(String arg[]) {
		// init de la sonde
		Probe probe = new Probe();
		probe.setName("test_rdds");
		probe.setTld("fr");
		probe.setType(ProbeType.Rdds);
		probe.setStatus(ProbeStatus.Ok);

		RddsProbe rddsProbe = new RddsProbe();
		rddsProbe.setWhoisNdd("afnic.fr");
		rddsProbe.setProbe(probe);

		rddsProbe.setServer("whois.nic.fr");

		rddsProbe.setTld("fr");

		rddsProbe.initParam();

		// démarrage sonde
		Thread probeTest = new Thread(rddsProbe);
		probeTest.start();

		// arret de la sonde
		try {
			Thread.sleep(660000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Stopping " + rddsProbe.getName());
		rddsProbe.getProbe().setStatus(ProbeStatus.Stopped);
		HeartBeat.sendBeat(rddsProbe.getProbe());

		// dnsProbe.launchTest();
	}
	// fin TEST
}
