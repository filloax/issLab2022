package it.unibo.radarSystemActor.main.local;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Util;
import it.unibo.radarSystemActor.common.ApplData;
import it.unibo.radarSystemActor.common.ControllerActor;
import it.unibo.radarSystemActor.common.LedActor;
import it.unibo.radarSystemActor.common.SonarActor;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
 

/*
 * Sistema che usa led e controller come attori locali
 */

public class UsingLedSonarControllerOnPc {
   

	public void doJob() {
		ColorsOut.outappl("ControllerUsingLedSonarOnPc | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		terminate();
	}
	

	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;
		DomainSystemConfig.sonarVerbose = true;
		CommSystemConfig.tracing        = false;

		new LedActor(ApplData.ledName);
		new ControllerActor( ApplData.controllerName );
		new SonarActor(ApplData.sonarName);
		
		//Creo altri Led per verificare che il numero di thread non aumenta
//		for( int i=1; i<=3; i++) {
//			new LedActor(ApplData.ledName+"_"+i);
//			Qak22Util.sendAMsg(ApplData.turnOnLed, ApplData.ledName+"_"+i  );
//			BasicUtils.delay(500);
//			Qak22Util.sendAMsg(ApplData.turnOffLed, ApplData.ledName+"_"+i  );
//		}
  	}
	
	protected void execute() {
  		Qak22Util.sendAMsg( ApplData.activateCrtl );
	} 

	public void terminate() {
		CommUtils.delay(10000);
		CommUtils.aboutThreads("Before exit - ");
		System.exit(0);
	}


	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new UsingLedSonarControllerOnPc().doJob();
		CommUtils.aboutThreads("Before end - ");
	}
}