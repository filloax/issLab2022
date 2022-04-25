package unibo.wenvUsage22.wshttp;

import org.json.JSONObject;
import org.json.JSONTokener;
import unibo.actor22comm.http.HttpConnection;
import unibo.actor22comm.interfaces.IObservable;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;

import java.util.Observable;

public class BoundaryWalkerWs implements IObserver {
	private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
	private  final int port                = 8090;
	private  final String HttpURL          = "http://"+localHostName+":"+port+"/api/move";
 

	private Interaction2021 conn;
	

	protected void doJob() throws Exception {
		conn = WsConnection.create("localhost:8091" );
		((IObservable)conn).addObserver(this);
		tryForward( ApplData.moveForward(500) );
 	}

	@Override
	public void update(String data) {
//		ColorsOut.out("BoundaryWalkerWs: Received " + data);
		JSONTokener tokener = new JSONTokener(data);
		JSONObject object = new JSONObject(tokener);

		if (object.has("collision")) {
			tryForward(ApplData.turnLeft(300));
		} else {
			tryForward(ApplData.moveForward(500));
		}

		// {"collision":"moveForward","target":"wallDown"}
		// {"endmove":true,"move":"moveForward"}
	}

	private void tryForward(String msg) {
		boolean success = false;
		for (int i = 0; i < 5 && !success; i++) {
			try {
				conn.forward(msg);
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
		}
		if (!success) {
			System.err.println("Too many attempts, exiting...");
			System.exit(-1);
		}
	}

	@Override
	public void update(Observable o, Object data) {
		update((String) data);
	}

	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
		new BoundaryWalkerWs().doJob();
		CommUtils.aboutThreads("At end - ");
	}

}
