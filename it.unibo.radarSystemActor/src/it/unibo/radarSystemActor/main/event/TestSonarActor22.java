package it.unibo.radarSystemActor.main.event;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystemActor.events.SonarActor22;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.Actor;
import unibo.actor22.annotations.AnnotUtil;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import it.unibo.radarSystemActor.common.ApplData;
import it.unibo.radarSystemActor.common.ControllerForSonarActor;
import it.unibo.radarSystemActor.common.RadarSystemConfig;

@Actor(name = ApplData.sonarName, implement = SonarActor22.class)
@Actor(name = ApplData.controllerName, implement = ControllerForSonarActor.class)
public class TestSonarActor22 {
	
	public TestSonarActor22() {
		configure();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   	= true;			
		DomainSystemConfig.tracing      	= true;
		DomainSystemConfig.sonarDelay   	= 200;
		DomainSystemConfig.sonarVerbose     = true;
		CommSystemConfig.tracing        	= true;
		
		//con false, il ControllerForSonarActor chiede la distanza, 
		//con true,  il ControllerForSonarActor agisce come observer
		RadarSystemConfig.sonarObservable 	= true;
		
		//ALTRO Observer oltr il Controller
// 		new EventObserver(ApplData.observerName);
// 		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evDistance);
		
		Qak22Context.handleLocalActorDecl(this);
		AnnotUtil.handleRepeatableActorDeclaration(this);
		if( RadarSystemConfig.sonarObservable  ) {
 			Qak22Context.registerAsEventObserver(ApplData.controllerName, ApplData.evDistance);
		}
 	}

	
	
	public void doJob() {
		Qak22Util.sendAMsg( ApplData.activateCrtl );
//		CommUtils.delay(3000);
//		Qak22Util.sendAMsg( ApplData.deactivateSonar );
		
	}

	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new TestSonarActor22().doJob();
		CommUtils.aboutThreads("Before end - ");
	}
}
