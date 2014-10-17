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

	boolean isInitDone = false;
	public RddsProbe(Probe probe, String tld, String whoisNdd) {
		super();
		this.probe = probe;
		this.tld = tld;
		this.whoisNdd = whoisNdd;

		initParam();
	}

	private RddsProbe() {
		this.probe = new Probe();
	}

	private void initParam() {
		rddsServiceAvailabilityParam = new RddsServiceAvailabilityParam(tld, whoisNdd);
		rddsQueryRttParam = new RddsQueryRttParam(tld, whoisNdd);
		this.isInitDone = true;
	}

	private static final Logger logger = LoggerFactory.getLogger(RddsProbe.class);

	private Probe probe;
	private String tld;
	private String whoisNdd;
	private int frequenceTest = 300000; /* 5 minutes */

	private static final long RDDS_QUERY_RTT = 2000;

	@Override
	public void launchTest() {
		if(!this.isInitDone){initParam();}
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
		this.probe.setTld(tld);
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

	public void setType(ProbeType type) {
		this.probe.setType(type);
	}

	public void setStatus(ProbeStatus status) {
		this.probe.setStatus(status);
	}

	public void setName(String name) {
		this.probe.setName(name);
	}

	// FIXME TEST

	public static RddsProbe getMockProbe() {
		// init de la sonde
		RddsProbe rddsProbe = new RddsProbe();
		rddsProbe.setName("test_rdds");
		rddsProbe.setTld("fr");
		
		rddsProbe.setWhoisNdd("afnic.fr");
		rddsProbe.setType(ProbeType.Rdds);
		rddsProbe.setStatus(ProbeStatus.Ok);



		rddsProbe.initParam();
		return rddsProbe;
	}

	@Override
    public String toString() {
	    return probe.toString();
    }

	/*
	 * public static void main(String arg[]) {
	 * 
	 * RddsProbe rddsProbe = getMockProbe(); // démarrage sonde Thread probeTest = new Thread(rddsProbe); probeTest.start();
	 * 
	 * // arret de la sonde try { Thread.sleep(660000); } catch (InterruptedException e) { e.printStackTrace(); }
	 * 
	 * rddsProbe.getProbe().stop(); // dnsProbe.launchTest(); }
	 */
	// fin TEST
}
