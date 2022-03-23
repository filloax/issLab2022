package it.unibo.radarSystem22.sprint2a.main.devicesOnRasp;

 
import alice.tuprolog.Int;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.comm2022.utils.CommSystemConfig;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.sprint2a.handlers.LedApplHandler;
import it.unibo.radarSystem22.sprint2a.handlers.SonarApplHandler;
import it.unibo.radarSystem22.sprint2a.proxy.SonarObserverClient;


/*
 * Attiva il TCPServer.
 * 
 */
public class RadarSysSprint2aDevicesOnRaspMain implements IApplication{
	private ISonar sonar;
	private ILed led;
	private TcpServer ledServer;
	private TcpServer sonarServer;

	@Override
	public void doJob(String domainConfig, String systemConfig) {
		setup(domainConfig,   systemConfig);
		configure();
		execute();
	}
	
	public void setup( String domainConfig, String systemConfig )  {
	    BasicUtils.aboutThreads(getName() + " | Before setup ");
	    CommSystemConfig.tracing            = true;
		if( domainConfig != null ) {
			DomainSystemConfig.setTheConfiguration(domainConfig);
		}
		if( systemConfig != null ) {
			RadarSystemConfig.setTheConfiguration(systemConfig);
		}
		if( domainConfig == null && systemConfig == null) {
			DomainSystemConfig.simulation  = true;
	    	DomainSystemConfig.testing     = false;			
	    	DomainSystemConfig.tracing     = false;			
			DomainSystemConfig.sonarDelay  = 200;
	    	DomainSystemConfig.ledGui      = true;		//se siamo su PC	
	
			RadarSystemConfig.radarGuiRemote = true;
		}
 
	}
	protected void configure() {		
	   led = DeviceFactory.createLed();
	   IApplMsgHandler ledHandler = LedApplHandler.create("ledHandler", led);
	   ledServer = new TcpServer("ledServer",RadarSystemConfig.ledPort,ledHandler );

	   sonar = DeviceFactory.createSonar();

		// Sonar normale: questo è un server che risponde a richieste del PC
		ColorsOut.out("Starting default sonar server...", ColorsOut.BLUE);
		IApplMsgHandler sonarHandler = SonarApplHandler.create("sonarHandler", sonar);
		sonarServer = new TcpServer("sonarServer",RadarSystemConfig.sonarPort, sonarHandler);

	   // Observable sonar: questo è un client che aggiorna il server di ascolto su PC
	   if (RadarSystemConfig.sonarObservable) {
		   if (!(sonar instanceof ISonarObservable)) {
			   throw new IllegalStateException("sonarObservable true but sonar is not observable");
		   }
		   ColorsOut.out("Connecting sonar observer client...", ColorsOut.BLUE);
		   SonarObserverClient sonarObserverClient = new SonarObserverClient(
				   "sonarObserverClient",
				   RadarSystemConfig.hostAddr,
				   Integer.toString(RadarSystemConfig.sonarObserverPort),
				   (ISonarObservable) sonar
		   );
	   }
	}
	
	protected void execute() {		
		ledServer.activate();
		sonarServer.activate();
	}
	
	@Override
	public String getName() {
		return this.getClass().getName() ;  
	}
	public ILed getLed() { return led; }
	public ISonar getSonar() { return sonar; }
	public void stop() {
		ledServer.deactivate();
		sonarServer.deactivate();
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with INIT CONFIG files| ");
		new RadarSysSprint2aDevicesOnRaspMain().doJob("../DomainSystemConfig.json","../RadarSystemConfig.json" );
  	}
}
