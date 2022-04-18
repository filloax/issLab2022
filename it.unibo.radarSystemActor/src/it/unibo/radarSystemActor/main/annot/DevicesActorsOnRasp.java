package it.unibo.radarSystemActor.main.annot;

 
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystemActor.common.LedActor;
import it.unibo.radarSystemActor.common.SonarActor;
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.ActorLocal;
import it.unibo.radarSystemActor.common.ApplData;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
  
@ActorLocal(
		name =      {"led", "sonar" }, 
		implement = {LedActor.class, SonarActor.class })

public class DevicesActorsOnRasp {
	private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl("DevicesActorsOnRasp | Start", ColorsOut.BLUE);
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
		
		Qak22Context.handleLocalActorDecl(this);
  
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
		new DevicesActorsOnRasp().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
