package it.unibo.radarSystem22.sprint2a.main.devicesOnRasp;


import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.utils.CommSystemConfig;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.ActionFunction;
import it.unibo.radarSystem22.sprint1.Controller;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.sprint2a.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.sprint2a.proxy.SonarProxyAsClient;

/*
 * Attiva il Controller (vedi sprint1) e il RadarDisplay (vedi domain)
 * e due proxy al Led e al Sonar.
 * 
 */
public class RadarSysSprint2aControllerOnPcMain implements IApplication{
	private IRadarDisplay radar;
	private ISonar sonar;
	private ILed  led ;
	private Controller controller;
	
	@Override
	public void doJob(String domainConfig, String systemConfig) {
		setup(domainConfig, systemConfig);
		configure();
		//start
	    ActionFunction endFun = (n) -> { 
	    	System.out.println(n); 
	    	terminate(); 
	    };
		controller.start(endFun, 100000);
	}
	
	public void setup(String domainConfig, String systemConfig)  {
		if( domainConfig != null ) {
			DomainSystemConfig.setTheConfiguration(domainConfig);
		}
		if( systemConfig != null ) {
			RadarSystemConfig.setTheConfiguration(systemConfig);
		}
		if( domainConfig == null && systemConfig == null) {
			DomainSystemConfig.testing = false;
			DomainSystemConfig.sonarDelay = 200;
			//Su PC
			DomainSystemConfig.simulation = true;

			RadarSystemConfig.DLIMIT = 12;
			RadarSystemConfig.radarGuiRemote = false;
			RadarSystemConfig.raspAddr = "192.168.1.15";

			CommSystemConfig.tracing = false;
		}
		// observer meno conveniente con proxy
		RadarSystemConfig.sonarObservable = false;
	}
	
	public void configure(  )  {	
 		ProtocolType protocol = ProtocolType.tcp;
		
 		led = new LedProxyAsClient("ledPxy",
 				RadarSystemConfig.raspAddr, Integer.toString(RadarSystemConfig.ledPort), protocol );
  		sonar = new SonarProxyAsClient("sonarPxy",
  				RadarSystemConfig.raspAddr, Integer.toString(RadarSystemConfig.sonarPort), protocol);
  		radar = DeviceFactory.createRadarDisplay();
 
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 		
	}
	public void terminate() {
 		BasicUtils.aboutThreads("Before deactivation | ");
		sonar.deactivate();
		System.exit(0);
	}	
	
	@Override
	public String getName() {
		return this.getClass().getName() ; //"RadarSystemSprint2OnPcMain";
	}
	//Get the system components 
	public IRadarDisplay getRadarGui() { return radar; }
	public ILed getLed() { return led; }
	public ISonar getSonar() { return sonar; }
	public Controller getController() { return controller; }

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with PC CONFIG files| ");
		new RadarSysSprint2aControllerOnPcMain().doJob( "../DomainSystemConfig.json","../RadarSystemConfig.json" );
  	}	
}
