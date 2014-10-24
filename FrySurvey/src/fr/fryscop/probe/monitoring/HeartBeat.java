package fr.fryscop.probe.monitoring;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.Probe;

public class HeartBeat extends AbstractHttpRequest {
	// appel la page de gestion des heartbeat en donnant l'état de la sonde
	// http://www.frysurvey.fr/monitoring/probes/beat.php?name=test&tld=fr&type=dns&status=1
	private static final Logger logger = LoggerFactory.getLogger(HeartBeat.class);
	private final static String defaultBeatUrl = "http://www.frysurvey.fr/monitoring/probes/beat.php";

	private static HeartBeat instance = null;

	private HeartBeat() {
	}

	protected synchronized static HeartBeat getInstance() {
		if (instance == null) {
			instance = new HeartBeat();
		}
		return instance;
	}

	public synchronized static void sendBeat(Probe probe) {
		String urlToCall = defaultBeatUrl + "?" + HeartBeatParameter.Name.getValue() + "=" + probe.getName() + "&" + HeartBeatParameter.Tld.getValue() + "=" + probe.getTld() + "&"
		        + HeartBeatParameter.Type.getValue() + "=" + probe.getType().getValue() + "&" + HeartBeatParameter.Status.getValue() + "=" + probe.getStatus().getValue();
		logger.debug("sendBeat() " + urlToCall);

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

	}

}
