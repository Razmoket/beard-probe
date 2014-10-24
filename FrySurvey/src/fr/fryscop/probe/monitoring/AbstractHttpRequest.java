package fr.fryscop.probe.monitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpRequest {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpRequest.class);
	public AbstractHttpRequest() {
		// TODO Auto-generated constructor stub
	}


	protected String post(String adress, List<String> keys, List<String> values, boolean getReturnContent) throws IOException {
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
