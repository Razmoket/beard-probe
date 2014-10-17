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

	public static void main(String args[]) {
		FTPClient ftp = new FTPClient();
		//	FTPClientConfig config = new FTPClientConfig();
		//config.setXXX(YYY); // change required options
		ftp.setControlKeepAliveTimeout(300);
		//ftp.configure(config );
		boolean error = false;
		String srv = "ftp.frysurvey.fr";
		String usr = "frysurvewg";
		String pwd = "Ym5YE9SeCDvZ";
		try {
			int reply;
			ftp.connect(srv);
			ftp.login(usr, pwd);
			System.out.println("Connected to " + srv + ".");
			System.out.print(ftp.getReplyString());

			// After connection attempt, you should check the reply code to verify
			// success.
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
			// do the job there
			System.out.println("Remote system is " + ftp.getSystemType());

			ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
	        ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
	        
			ftp.enterLocalPassiveMode();
			ftp.setCopyStreamListener(createListener());
		
			String localhostName = InetAddress.getLocalHost().getHostName();
			String local = "archive-log-probe/probe-log-file.20141015.log.zip";
			String remote = "/log_probes/" + localhostName + "_probe-log-file.20141015.log.zip";

			InputStream input;
			input = new FileInputStream(local);

			System.out.println("local:"+local);
			System.out.println("remote:"+remote);
			boolean storeFile = ftp.storeFile(remote, input);
			System.out.println("storeFile:" + storeFile);
			reply = ftp.getReplyCode();
			System.out.println("reply:"+reply);
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
			System.exit(error ? 1 : 0);
		}
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
}
