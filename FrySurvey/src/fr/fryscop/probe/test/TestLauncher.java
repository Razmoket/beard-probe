package fr.fryscop.probe.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.test.dns.DnsProbe;
import fr.fryscop.probe.test.rdds.RddsProbe;

public class TestLauncher {

	private static final Logger logger = LoggerFactory.getLogger(TestLauncher.class);
	private List<ProbeTest> probeList;

	private Map<ProbeTest, Thread> launchedProbe = new HashMap<ProbeTest, Thread>();

	public TestLauncher() {
	}

	public TestLauncher(List<ProbeTest> probes) {
		this.probeList = probes;
		initProbeList();
	}

	public void initProbeList() {
		logger.info("Initializing Probes Threads List");
		for (ProbeTest probe : probeList) {
			Thread thread = new Thread(probe);
			logger.debug("initProbeList- probe.toString():" + probe.toString());
			thread.setName(probe.toString());
			launchedProbe.put(probe, thread);
		}
	}

	public void startProbes() {
		logger.info("Starting Probes Threads List");
		for (ProbeTest probe : probeList) {
			Thread thread = launchedProbe.get(probe);
			if (thread != null) {
				thread.start();
			}
		}
	}

	public void stopProbes() {
		logger.info("Stopping Probes Threads List");
		for (ProbeTest probe : probeList) {
			probe.getProbe().stop();
		}
	}

	public List<ProbeTest> getProbeList() {
		return probeList;
	}

	public void setProbeList(List<ProbeTest> probeList) {
		this.probeList = probeList;
	}

	// FIXME TEst
	public static void main(String arg[]) {
		ArrayList<ProbeTest> probes = new ArrayList<ProbeTest>();
		probes.add(RddsProbe.getMockProbe());
		probes.add(DnsProbe.getMockProbe());

		TestLauncher testLauncher = new TestLauncher(probes);
		testLauncher.startProbes();

		// arret de la sonde
		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		testLauncher.stopProbes();
	}

}
