package Background;

public abstract class BackgroundTask extends Thread {
	 volatile protected boolean running = true;

	 public BackgroundTask() {
		super();
		// TODO Auto-generated constructor stub
	}



	public BackgroundTask(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}



	public abstract void kill();

}
