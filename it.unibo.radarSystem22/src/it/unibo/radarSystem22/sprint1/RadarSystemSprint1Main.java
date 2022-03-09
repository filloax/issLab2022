package it.unibo.radarSystem22.sprint1;
 
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


/*
 *  
 */

public class RadarSystemSprint1Main implements IApplication{
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	@Override
	public String getName() {	 
		return "RadarSystemSprint1Main";
	}

	public void setup( String configFile )  {
		if( configFile != null ) DomainSystemConfig.setTheConfiguration(configFile);
		else {
  			DomainSystemConfig.testing      	= false;			
			DomainSystemConfig.sonarDelay       = 200;
			//Su PC
			DomainSystemConfig.simulation   	= true;
			DomainSystemConfig.DLIMIT      		= 40;  
			DomainSystemConfig.ledGui           = true;
			RadarSystemConfig.RadarGuiRemote    = false;
		//Su Raspberry (nel file di configurazione)
//			DomainSystemConfig.simulation   		= false;
//			DomainSystemConfig.DLIMIT      		= 12;  
//			DomainSystemConfig.ledGui            = false;
//			RadarSystemConfig.RadarGuiRemote    = true;
		}
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		//start
	    ActionFunction endFun = (n) -> { 
	    	BasicUtils.aboutThreads("Controller endFun | ");
	    	System.out.println(n); 
	    	terminate(); 
	    };
		controller.start(endFun, 30);
	}
	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    radar      = RadarSystemConfig.RadarGuiRemote ? null : DeviceFactory.createRadarGui();
		BasicUtils.aboutThreads("Before Controller creation | ");
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		BasicUtils.aboutThreads("Before termination | ");
		sonar.deactivate();
		System.exit(0);
	}

//Get the system components 
 	public IRadarDisplay getRadarGui() { return radar; }
 	public ILed getLed() { return led; }
 	public ISonar getSonar() { return sonar; }
 	public Controller getController() { return controller; }
	
	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT | ");
		new RadarSystemSprint1Main().doJob(null);
		
 	}

}
