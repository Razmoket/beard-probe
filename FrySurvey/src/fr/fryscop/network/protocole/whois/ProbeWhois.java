package fr.fryscop.network.protocole.whois;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.net.whois.WhoisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbeWhois {
	private static final Logger logger = LoggerFactory.getLogger(ProbeWhois.class);

	public static long request(String[] args) {
		int index;
		String handle, host;
		InetAddress address = null;
		WhoisClient whois;
		long startTime, endTime;

		if (args.length != 1) {
			logger.error("usage: fwhois handle[@<server>]");
			return -1;
		}

		index = args[0].lastIndexOf("@");

		whois = new WhoisClient();
		// We want to timeout if a response takes longer than 60 seconds
		whois.setDefaultTimeout(60000);

		if (index == -1) {
			handle = args[0];
			host = WhoisClient.DEFAULT_HOST;
		} else {
			handle = args[0].substring(0, index);
			host = args[0].substring(index + 1);
		}

		startTime = System.currentTimeMillis();
		try {
			address = InetAddress.getByName(host);
			logger.trace("[" + address.getHostName() + "]");
		} catch (UnknownHostException e) {
			logger.error("Error unknown host: " + e.getMessage());
			endTime = System.currentTimeMillis();
			return endTime - startTime;
		}

		try {
			whois.connect(address);
			String query = whois.query(handle);
			logger.trace(query);
			whois.disconnect();
		} catch (IOException e) {
			logger.error("Error I/O exception: " + e.getMessage());
			endTime = System.currentTimeMillis();
			return endTime - startTime;
		}
		endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
}
