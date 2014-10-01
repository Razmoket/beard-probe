package fr.fryscop.probe.monitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import fr.fryscop.probe.Probe;

public class HeartBeat {
	// appel la page de gestion des heartbeat en donnant l'état de la sonde
	// http://www.frysurvey.fr/monitoring/probes/beat.php?name=test&tld=fr&type=dns&status=1
	private final static String defaultBeatUrl = "http://www.frysurvey.fr/monitoring/probes/beat.php?";

	private static HeartBeat instance = null;

	private HeartBeat() {
	}

	protected static HeartBeat getInstance() {
		if (instance == null) {
			instance = new HeartBeat();
		}
		return instance;
	}

	public void sendBeat(Probe probe) {
		String urlToCall = defaultBeatUrl + HeartBeatParameter.Name.getValue() + "=" + probe.getName() + HeartBeatParameter.Tld.getValue() + "=" + probe.getTld()
		        + HeartBeatParameter.Type.getValue() + "=" + probe.getType().getValue() + HeartBeatParameter.Status.getValue() + "=" + probe.getStatus().getValue();
		System.out.println(urlToCall);

		try {
			HeartBeat.getInstance().get(urlToCall, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private  String get(String url, boolean getReturnContent) throws IOException {
		String source = "";
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		if (getReturnContent) {
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				source += inputLine;
			in.close();
		}
		return source;
	}

	private  String post(String adress, List<String> keys, List<String> values, boolean getReturnContent) throws IOException {
		String result = "";
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		try {
			// encodage des paramètres de la requête
			String data = "";
			for (int i = 0; i < keys.size(); i++) {
				if (i != 0)
					data += "&amp;";
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
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

}
