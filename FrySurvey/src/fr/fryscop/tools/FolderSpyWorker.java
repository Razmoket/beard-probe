package fr.fryscop.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Surveiller les changements dans un dossier
 * 
 */
public class FolderSpyWorker implements Runnable {

	private Path	path	         = null;
	private FtpSync	ftp	             = new FtpSync("ftp.frysurvey.fr", "frysurvewg", "Ym5YE9SeCDvZ");

	private String	pathname	     = null;
	private String	backupFolderName	= "backup/";

	/**
	 * Constructor
	 * 
	 * @param pathname
	 *            String dossier à surveiller
	 */
	public FolderSpyWorker(String pathname) {
		this.pathname = pathname;
		this.path = Paths.get(this.pathname);
		System.out.println("Start FolderSpy on:" + pathname);

	}

	public FolderSpyWorker() {
		this("archive-log-probe/");
	}

	/**
	 * Worker
	 */
	public void run() {
		WatchService watchService = null;
		try {
			watchService = this.path.getFileSystem().newWatchService();
			//Enregistrer les opérations à surveiller
			this.path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

			WatchKey watchKey;
			// boucle sans fin
			while (!Thread.interrupted()) {
				watchKey = watchService.take();

				//traiter les evenements
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					String fileName = event.context().toString();
					if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
						System.out.println("new file create " + fileName);
						if (!ftp.doJob(fileName)) {
							// le fichier a ete transfere correctement, il est supprimable ou bougeable dans un autre dossier
							File source = new File(this.pathname + fileName);
							File destination = new File(this.pathname + backupFolderName + fileName);
							source.renameTo(destination);
						}
					} else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
						System.out.println(fileName + " has been modified");
					} else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
						System.out.println(fileName + " has been deleted");
					} else if (StandardWatchEventKinds.OVERFLOW.equals(event.kind())) {
						System.out.println("Strange event");
						continue;
					}
				}
				//se place en attente de message
				watchKey.reset();
			}
		} catch (InterruptedException ex) {
			try {
				if (watchService != null) watchService.close();
				System.out.println("Stop FolderSpy");
			} catch (IOException ex1) {
				Logger.getLogger(FolderSpyWorker.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (Exception ex) {
			Logger.getLogger(FolderSpyWorker.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public FtpSync getFtp() {
		return ftp;
	}

	public void setFtp(FtpSync ftp) {
		this.ftp = ftp;
	}
}