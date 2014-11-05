package fr.fryscop.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpSync {
	private static final Logger logger = LoggerFactory.getLogger(FtpSync.class);

	String server; // = "ftp.frysurvey.fr";
	String username; // = "frysurvewg";
	String password; // = "Ym5YE9SeCDvZ";

	String default_remote_folder = "/log_probes/";
	String default_local_folder = "archive-log-probe/";

	public FtpSync(String server, String username, String password) {
		super();
		this.server = server;
		this.username = username;
		this.password = password;
	}

	public boolean doJob(String localFilename) {
		FTPClient ftp = new FTPClient();
		ftp.setControlKeepAliveTimeout(300);
		boolean error = false;
		try {
			int reply;
			ftp.connect(server);
			ftp.login(username, password);
			logger.info("Connected to " + server + ".");
			logger.info(ftp.getReplyString());

			// After connection attempt, you should check the reply code to verify
			// success.
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				logger.error("FTP server refused connection.");
				return true;
			}
			// do the job there
			logger.info("Remote system is " + ftp.getSystemType());

			ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
			ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

			ftp.enterLocalPassiveMode();
			ftp.setCopyStreamListener(createListener());

			String localhostName = null;
			try {
				localhostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				if (System.getProperty("os.name").startsWith("Windows")) {
					// Windows will always set the 'COMPUTERNAME' variable
					localhostName = System.getenv("COMPUTERNAME");
				} else {
					// If it is not Windows then it is most likely a Unix-like operating system
					// such as Solaris, AIX, HP-UX, Linux or MacOS.

					// Most modern shells (such as Bash or derivatives) sets the
					// HOSTNAME variable so lets try that first.
					String hostname = System.getenv("HOSTNAME");
					if (hostname != null) {
						localhostName = hostname;
					} else {

						// If the above returns null *and* the OS is Unix-like
						// then you can try an exec() and read the output from the
						// 'hostname' command which exist on all types of Unix/Linux.
						Process proc = Runtime.getRuntime().exec("hostname");
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
						String s = stdInput.readLine();
						if (s != null) {
							localhostName = s;
						} else {
							logger.error("We get an error when trying to retreive host name:\n");
							while ((s = stdError.readLine()) != null) {
								logger.error(s);
							}
						}
					}
				}
			}
			String local = default_local_folder + localFilename; // "probe-log-file.20141015.log.zip";
			String remote = default_remote_folder + localhostName + "_" + localFilename;// probe-log-file.20141015.log.zip";

			InputStream input;
			input = new FileInputStream(local);

			ftp.storeFile(remote, input);
			reply = ftp.getReplyCode();
			input.close();

			// end job
			ftp.logout();
		} catch (IOException e) {
			error = true;
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					// do nothing
				}
			}
		}
		return error;
	}

	private static CopyStreamListener createListener() {
		return new CopyStreamListener() {
			private long megsTotal = 0;

			// @Override
			public void bytesTransferred(CopyStreamEvent event) {
				bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
			}

			// @Override
			public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
				long megs = totalBytesTransferred / 1000000;
				for (long l = megsTotal; l < megs; l++) {
					logger.error("#");
				}
				megsTotal = megs;
			}
		};
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
