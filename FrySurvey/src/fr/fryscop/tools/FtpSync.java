package fr.fryscop.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

public class FtpSync {

	String	server;	                                        //                  = "ftp.frysurvey.fr";
	String	username;	                                    //                  = "frysurvewg";
	String	password;	                                    //                  = "Ym5YE9SeCDvZ";

	String	default_remote_folder	= "/log_probes/";
	String	default_local_folder	= "archive-log-probe/";

	
	
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
			System.out.println("Connected to " + server + ".");
			System.out.print(ftp.getReplyString());

			// After connection attempt, you should check the reply code to verify
			// success.
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				return true;
			}
			// do the job there
			System.out.println("Remote system is " + ftp.getSystemType());

			ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
			ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

			ftp.enterLocalPassiveMode();
			ftp.setCopyStreamListener(createListener());

			String localhostName = InetAddress.getLocalHost().getHostName();
			String local = default_local_folder + localFilename; //"probe-log-file.20141015.log.zip";
			String remote = default_remote_folder + localhostName + "_" + localFilename;//probe-log-file.20141015.log.zip";

			InputStream input;
			input = new FileInputStream(local);

			System.out.println("local:" + local);
			System.out.println("remote:" + remote);
			boolean storeFile = ftp.storeFile(remote, input);
			System.out.println("storeFile:" + storeFile);
			reply = ftp.getReplyCode();
			System.out.println("reply:" + reply);
			input.close();

			// end job
			ftp.logout();
		} catch (IOException e) {
			error = true;
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
			private long	megsTotal	= 0;

			//            @Override
			public void bytesTransferred(CopyStreamEvent event) {
				bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
			}

			//            @Override
			public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
				long megs = totalBytesTransferred / 1000000;
				for (long l = megsTotal; l < megs; l++) {
					System.err.print("#");
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
