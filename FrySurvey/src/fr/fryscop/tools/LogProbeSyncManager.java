package fr.fryscop.tools;

public class LogProbeSyncManager {

	

	public LogProbeSyncManager() {

	}

	public static void main(String[] args) throws InterruptedException {
        //Cr�er le worker
        FolderSpyWorker folderSpyWorker=new FolderSpyWorker();
        //Cr�er le thred
        Thread folderSpyThread = new Thread(folderSpyWorker);
        //Lancer le thread
        folderSpyThread.start();         
        
    }
}
