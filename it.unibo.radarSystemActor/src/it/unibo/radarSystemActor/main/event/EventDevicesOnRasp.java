package it.unibo.radarSystemActor.main.event;


import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystemActor.common.ApplData;
import it.unibo.radarSystemActor.common.LedActor;
import it.unibo.radarSystemActor.common.RadarSystemConfig;
import it.unibo.radarSystemActor.common.SonarActor;
import it.unibo.radarSystemActor.events.SonarActor22;
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.Actor;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;


@Actor(name="led", implement = LedActor.class)
@Actor(name="sonar", implement = SonarActor22.class)
public class EventDevicesOnRasp {
	private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl(getClass().getName() + " | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = true;
		DomainSystemConfig.sonarVerbose = true;
		CommSystemConfig.tracing        = false;
		RadarSystemConfig.sonarObservable = true;
		
		Qak22Context.handleLocalActorDecl(this);
		Qak22Context.handleRepeatableActorDeclaration(this);

		ctx = new EnablerContextForActors( "ctx",ApplData.ctxPort,ApplData.protocol);
		//new LedActor( ApplData.ledName );
 		//Registrazione dei componenti presso il contesto: NO MORE ... 
  	}
	
	protected void execute() {
		ColorsOut.outappl("DevicesActorsOnRasp | execute", ColorsOut.MAGENTA);
		ctx.activate();
	} 

	public void terminate() {
		CommUtils.aboutThreads("Before exit - ");
// 	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new EventDevicesOnRasp().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
