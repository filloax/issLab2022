package it.unibo.radarSystem22.sprint1;

import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.main.IApplication;

public class RadarSystemSprint1Main implements IApplication {
    private ISonar sonar = null;
    private ILed led = null;
    private IRadarDisplay radar = null;
    private Controller controller;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void doJob(String domainCfgFile, String systemCfgFile) {
        setup(domainCfgFile, systemCfgFile);
        configure();
        ActionFunction endFun = (n) -> {
            System.out.println(n);
            terminate();
        };
        controller.start(endFun, 1000); // termina tramite Ctrl-c idealmente
    }

    public void terminate() {
        BasicUtils.aboutThreads("Before deactivation | ");
        sonar.deactivate();
        System.exit(0);
    }

    public void setup(String domainCfgFile, String systemCfgFile)  {
        if( domainCfgFile != null )
            DomainSystemConfig.setTheConfiguration(domainCfgFile);
        else { //default
            DomainSystemConfig.testing         = false;
            DomainSystemConfig.tracing         = true; // cambiare print verbose per non dipendere da qua
            DomainSystemConfig.sonarDelay      = 200;
            //Su PC
            DomainSystemConfig.simulation      = true;
            DomainSystemConfig.ledGui          = true;
            DomainSystemConfig.sonarVerbose    = true;
            DomainSystemConfig.radarAvailable  = false;
            DomainSystemConfig.sonarDistanceMax = 1000;
            DomainSystemConfig.sonarMockStartDist = 100;
            DomainSystemConfig.sonarMockDelta = -1;
            //Su Raspberry (nel file di configurazione)
            //DomainSystemConfig.simulation    = false;
            //DomainSystemConfig.DLIMIT        = 12;
            //DomainSystemConfig.ledGui        = false;
            //DomainSystemConfig.RadarGuiRemote = true;
        }
        if (systemCfgFile != null) {
            RadarSystemConfig.setTheConfiguration(systemCfgFile);
        } else {
            RadarSystemConfig.DLIMIT = 40;
        }
    }

    protected void configure() {
        //Dispositivi di Input
        sonar = DeviceFactory.createSonar();
        //Dispositivi di Output
        led   = DeviceFactory.createLed();
        radar = (DomainSystemConfig.radarAvailable && !DomainSystemConfig.radarRemote) ?
            DeviceFactory.createRadarDisplay() : null;
        BasicUtils.aboutThreads("Before Controller creation | ");
        //Controller
        controller = Controller.create(led, sonar, radar);
    }

    public IRadarDisplay getRadarGui() { return radar; }
    public ILed getLed() { return led; }
    public ISonar getSonar() { return sonar; }
    public Controller getController() { return controller; }

    public static void main(String[] args) {
        new RadarSystemSprint1Main().doJob("DomainSystemConfig.json", "RadarSystemConfig.json"); //su PC

        //su Rasp:
        //new RadarSystemSprint1Main().doJob("DomainSystemConfig.json");
    }
}