package fr.fryscop.network.protocole.whois;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.net.whois.WhoisClient;

public class ProbeWhois {
	
	public static long request(String[] args) {
		int index;
		String handle, host;
		InetAddress address = null;
		WhoisClient whois;
		long startTime, endTime;
		

		if (args.length != 1) {
			System.err.println("usage: fwhois handle[@<server>]");
			return -1;
		}

		index = args[0].lastIndexOf("@");

		whois = new WhoisClient();
		// We want to timeout if a response takes longer than 60 seconds
		whois.setDefaultTimeout	(60000);

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
			System.out.println("[" + address.getHostName() + "]");
		} catch (UnknownHostException e) {
			System.err.println("Error unknown host: " + e.getMessage());
			endTime = System.currentTimeMillis();
			return endTime - startTime;
		}

		try {
			whois.connect(address);
			System.out.print(whois.query(handle));
			whois.disconnect();
		} catch (IOException e) {
			System.err.println("Error I/O exception: " + e.getMessage());
			endTime = System.currentTimeMillis();
			return endTime - startTime;
		}
		endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
}