package it.unibo.radarSystemActor.main.event;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystemActor.common.ApplData;
import it.unibo.radarSystemActor.common.ControllerActor;
import it.unibo.radarSystemActor.common.ControllerForSonarActor;
import it.unibo.radarSystemActor.common.RadarSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.Actor;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;


@Actor(name="controller", implement = ControllerForSonarActor.class)
@Actor(name="led", local = false, host = ApplData.raspAddr, port = ""+ApplData.ctxPort)
@Actor(name="sonar", local = false, host = ApplData.raspAddr, port = ""+ApplData.ctxPort)
public class EventControllerOnPc {

	public void doJob() {
		ColorsOut.outappl(getClass().getName() + " | Start", ColorsOut.BLUE);
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
		RadarSystemConfig.sonarObservable = true;
		ProtocolType protocol 		    = CommSystemConfig.protcolType;
		
//		new EventObserver(ApplData.observerName);
//		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evEndWork);
		
		Qak22Context.handleLocalActorDecl(this);
 		Qak22Context.handleRemoteActorDecl(this);
		Qak22Context.handleRepeatableActorDeclaration(this);
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
		new EventControllerOnPc().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
