package fr.fryscop.probe.monitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.Probe;

public class HeartBeat {
	// appel la page de gestion des heartbeat en donnant l'état de la sonde
	// http://www.frysurvey.fr/monitoring/probes/beat.php?name=test&tld=fr&type=dns&status=1
	private static final Logger logger = LoggerFactory.getLogger(HeartBeat.class);
	private final static String defaultBeatUrl = "http://www.frysurvey.fr/monitoring/probes/beat.php";

	private static HeartBeat instance = null;

	private HeartBeat() {
	}

	protected static HeartBeat getInstance() {
		if (instance == null) {
			instance = new HeartBeat();
		}
		return instance;
	}

	public static void sendBeat(Probe probe) {
		String urlToCall = defaultBeatUrl +"?"+ HeartBeatParameter.Name.getValue() + "=" + probe.getName() + "&" + HeartBeatParameter.Tld.getValue() + "=" + probe.getTld() + "&"
		        + HeartBeatParameter.Type.getValue() + "=" + probe.getType().getValue() + "&" + HeartBeatParameter.Status.getValue() + "=" + probe.getStatus().getValue();
		logger.debug("sendBeat() "+urlToCall);

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


	private String post(String adress, List<String> keys, List<String> values, boolean getReturnContent) throws IOException {
		String result = "";
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		try {
			// encodage des paramètres de la requête
			String data = "";
			for (int i = 0; i < keys.size(); i++) {
				if (i != 0)
					data += "&";
				data += URLEncoder.encode(keys.get(i), "UTF-8") + "=" + URLEncoder.encode(values.get(i), "UTF-8");
			}
			// création de la connection
			URL url = new URL(adress);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			// envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();

			if (getReturnContent) {
				// lecture de la réponse
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String ligne;
				while ((ligne = reader.readLine()) != null) {
					result += ligne;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			try {
				reader.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

}
