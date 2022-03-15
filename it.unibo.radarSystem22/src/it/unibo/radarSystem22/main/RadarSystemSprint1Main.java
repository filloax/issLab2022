package it.unibo.radarSystem22.main;

import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.ActionFunction;
import it.unibo.radarSystem22.sprint1.Controller;

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
    public void doJob(String configFileName) {
        setup(configFileName);
        configure();
        ActionFunction endFun = (n) -> {
            System.out.println(n);
            terminate();
        };
        //start
        controller.start(endFun, 110);
    }

    public void terminate() {
        BasicUtils.aboutThreads("Before deactivation | ");
        System.out.println("Sonar still active: " + sonar.isActive());
        sonar.deactivate();
        System.exit(0);
    }

    public void setup(String configFile)  {
        if( configFile != null )
            DomainSystemConfig.setTheConfiguration(configFile);
        else { //default
            DomainSystemConfig.testing         = false;
            DomainSystemConfig.sonarDelay      = 20;
            //Su PC
            DomainSystemConfig.simulation      = true;
            DomainSystemConfig.DLIMIT          = 40;
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
    }

    protected void configure() {
        //Dispositivi di Input
        sonar = DeviceFactory.createSonar();
        //Dispositivi di Output
        led   = DeviceFactory.createLed();
        radar = DomainSystemConfig.radarAvailable ?
            DeviceFactory.createRadarDisplay() : null;
        BasicUtils.aboutThreads("Before Controller creation | ");
        //Controller
        controller = Controller.create(led, sonar, radar);
    }

    public static void main(String[] args) throws Exception {
        new RadarSystemSprint1Main().doJob(null); //su PC
        //su Rasp:
        //new RadarSystemSprint1Main().doJob("DomainSystemConfig.json");
    }
}