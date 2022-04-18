package it.unibo.radarSystemActor.main.annot;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystemActor.common.ControllerActor;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.ActorRemote;
import it.unibo.radarSystemActor.common.ApplData;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
 
 
 
/*
 * Questo sistema ipotizza che led e sonar siano attori 
 * con cui interagire  a scambio di messaggi
 * ATTIVARE LedActorOnRasp
 */
@ActorLocal(name =     {"controller" }, 
           implement = {ControllerActor.class })
@ActorRemote(name =   {"led","sonar"}, 
             host=    {ApplData.raspAddr,ApplData.raspAddr}, 
             port=    { ""+ApplData.ctxPort, ""+ApplData.ctxPort}, 
             protocol={ "TCP" , "TCP" })
public class UsingActorsWithAnnotOnPc {

	public void doJob() {
		ColorsOut.outappl("UsingActorsWithAnnotOnPc | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		//terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.tracing      = false;			
 		CommSystemConfig.protcolType    = ProtocolType.tcp;
		CommSystemConfig.tracing        = false;
		ProtocolType protocol 		    = CommSystemConfig.protcolType;
		
//		new EventObserver(ApplData.observerName);
//		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evEndWork);
		
		Qak22Context.handleLocalActorDecl(this);
 		Qak22Context.handleRemoteActorDecl(this);
 	}
	
	protected void execute() {
		ColorsOut.outappl("UsingActorsWithAnnotOnPc | execute", ColorsOut.MAGENTA);
//		for( int i=1; i<=2; i++) {
//			Qak22Util.sendAMsg( ApplData.turnOnLed  );
//	 	    CommUtils.delay(500);
//	 	    Qak22Util.sendAMsg( ApplData.turnOffLed  );
//	 	    CommUtils.delay(500);
//		}
//		
//
		Qak22Util.sendAMsg( ApplData.activateCrtl );

		BasicUtils.delay(10 * 1000);
		System.exit(0);
	} 
	
	public void terminate() {
		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new UsingActorsWithAnnotOnPc().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
