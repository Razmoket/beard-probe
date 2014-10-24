package fr.fryscop.probe.monitoring;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.Probe;

public class ProbeListener extends AbstractHttpRequest{

	private static final Logger logger = LoggerFactory.getLogger(ProbeListener.class);
	private final static String defaultBeatUrl = "http://www.frysurvey.fr/monitoring/probes/probe_add.php";
	private static ProbeListener instance = null;

	private ProbeListener() {
	}

	protected synchronized static ProbeListener getInstance() {
		if (instance == null) {
			instance = new ProbeListener();
		}
		return instance;
	}

	public synchronized static void createProbeListener(Probe probe) {
		String urlToCall = defaultBeatUrl + "?" + HeartBeatParameter.Name.getValue() + "=" + probe.getName() + "&" + HeartBeatParameter.Tld.getValue() + "=" + probe.getTld() + "&"
		        + HeartBeatParameter.Type.getValue() + "=" + probe.getType().getValue() + "&" + HeartBeatParameter.Status.getValue() + "=" + probe.getStatus().getValue();
		logger.debug("createProbeListener() " + urlToCall);

		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();

		keys.add(HeartBeatParameter.Name.getValue());
		keys.add(HeartBeatParameter.Tld.getValue());
		keys.add(HeartBeatParameter.Type.getValue());
		keys.add(HeartBeatParameter.Status.getValue());

		values.add(probe.getName());
		values.add(probe.getTld());
		values.add(probe.getType().getValue());
		values.add(probe.getStatus().getValue());

		try {
			HeartBeat.getInstance().post(defaultBeatUrl, keys, values, true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
	        Thread.sleep(1000);
	        //temporisation pour etre sûr que tout passe
        } catch (InterruptedException e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
        }

	}

}
