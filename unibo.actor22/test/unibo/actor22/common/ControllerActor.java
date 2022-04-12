package unibo.actor22.common;
 
import it.unibo.kactor.IApplMessage;
import unibo.actor22.*;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

/*
 * Il controller conosce SOLO I NOMI dei dispositivi 
 * (non ha riferimenti ai dispositivi-attori)
 */
public class ControllerActor extends QakActor22{
	protected int numIter = 1;
	protected IApplMessage getStateRequest;
	protected IApplMessage getDistanceRequest;
	protected IApplMessage activateSonar;
	protected IApplMessage deactivateSonar;
	protected final int dlimit;
	protected String justAsked;

	public ControllerActor(String name  ) {
		super(name);
		getStateRequest  = Qak22Util.buildRequest(name,"ask", ApplData.reqLedState, ApplData.ledName);
		getDistanceRequest = Qak22Util.buildRequest(name, "ask", ApplData.reqDistance, ApplData.sonarName);
		activateSonar = Qak22Util.buildDispatch(name, "cmd", ApplData.cmdActivate, ApplData.sonarName);
		deactivateSonar = Qak22Util.buildDispatch(name, "cmd", ApplData.cmdDeactivate, ApplData.sonarName);
		dlimit = 40;
		justAsked = null;
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {  
		if( msg.isReply() ) {
			elabAnswer(msg);
		}else { 
			elabCmd(msg) ;	
		}
 	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case ApplData.cmdActivate : {
				doControllerWork();
	 			break;
			}
			default:
				break;
		}		
	}
	
	protected void wrongBehavior() {
  	    //WARNING: Inviare un treno di messaggi VA EVITATO
		//mantiene il controllo del Thread degli attori (qaksingle)		
		for( int i=1; i<=3; i++) {
			forward( ApplData.turnOffLed );
			CommUtils.delay(500);
			forward( ApplData.turnOnLed );
			CommUtils.delay(500);		
		}
		forward( ApplData.turnOffLed );
	}

	protected void doControllerWork() { doControllerWork(-1); }

    protected void doControllerWork(int distanceVal) {
		CommUtils.aboutThreads(getName()  + " |  Before doControllerWork dist=" + distanceVal);
		//wrongBehavior();
  		//ColorsOut.outappl( getName()  + " | numIter=" + numIter  , ColorsOut.GREEN);		
	    if(numIter++ < 100) {
			if (distanceVal >= 0)
				if(distanceVal < dlimit)
					forward(ApplData.turnOnLed); //accesione
				else
					forward(ApplData.turnOffLed); //spegnimento
			else
				forward(activateSonar);
			justAsked = getDistanceRequest.msgContent();
	        request(getDistanceRequest);
	  	} else {
	    	  forward(ApplData.turnOffLed);
			  forward(deactivateSonar);
			  //ColorsOut.outappl(getName() + " | emit " + ApplData.endWorkEvent, ColorsOut.MAGENTA);
	    	  //emit( ApplData.endWorkEvent );
	  	}
	}
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer numIter=" + numIter + " "+ msg, ColorsOut.MAGENTA);
		CommUtils.delay(500);

		if (msg.msgSender().equals(ApplData.sonarName) && justAsked.equals(ApplData.reqDistance))
			doControllerWork(Integer.parseInt(msg.msgContent()));
	}

}
