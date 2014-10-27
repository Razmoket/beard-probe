package fr.fryscop.tools;

public class LogProbeSyncManager {

	

	public LogProbeSyncManager() {

	}

	public static void start(){
		//Créer le worker
        FolderSpyWorker folderSpyWorker=new FolderSpyWorker();
        //Créer le thred
        Thread folderSpyThread = new Thread(folderSpyWorker);
        //Lancer le thread
        folderSpyThread.start();         
    }
}
