package it.unibo.radarSystem22.sprint2a.main.devicesOnRasp;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.ActionFunction;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.sprint1.RadarSystemSprint1Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Sprint2aTest {
    private RadarSysSprint2aControllerOnPcMain sysPc;
    private RadarSysSprint2aDevicesOnRaspMain sysRasPi;
    private Thread threadPc;
    private Thread threadRasPi;

    @Before
    public void setUp() {
        System.out.println("setUp");
        try {
            sysPc = new RadarSysSprint2aControllerOnPcMain();
            sysRasPi = new RadarSysSprint2aDevicesOnRaspMain();
            sysPc.setup( null,null );
            sysRasPi.setup(null, null);

            DomainSystemConfig.simulation       = true;
            DomainSystemConfig.testing    		= true;
            DomainSystemConfig.tracing    		= true;
            DomainSystemConfig.sonarVerbose     = true;
            RadarSystemConfig.hostAddr          = "localhost";
            RadarSystemConfig.raspAddr          = "localhost";
            RadarSystemConfig.sonarObservable   = false;
            DomainSystemConfig.radarAvailable   = false; // problemi a chiudere automaticamente la finestra

            threadRasPi = new Thread(() -> {
                sysRasPi.configure();
                sysRasPi.execute();
            });
            threadRasPi.start();
            ColorsOut.out("TEST RASPI THREAD STARTED", ColorsOut.YELLOW);
            //threadPc = new Thread(() -> {
                sysPc.configure();
            //});
            //threadPc.start();
            //ColorsOut.out("TEST PC THREAD STARTED", ColorsOut.YELLOW);

            BasicUtils.delay(100);
        } catch (Exception e) {
            fail("setup ERROR " + e.getMessage() );
        }
    }

    @After
    public void endTest() {
        sysRasPi.stop();
        System.out.println("endTest");
    }


    @Test
    public void testFarDistance() {
        DomainSystemConfig.sonarMockStartDist = RadarSystemConfig.DLIMIT +20;
        testTheDistance( false );
    }

    @Test
    public void testNearDistance( ) {
        DomainSystemConfig.sonarMockStartDist = RadarSystemConfig.DLIMIT - 5;
        testTheDistance( true );
    }

    private boolean done = false;

    public void setDone(boolean done) { this.done = done; }

    // synchronized a causa di done condiviso per ragione sott, nel caso
    // questo oggetto sia condiviso tra più thread
    protected synchronized void testTheDistance( boolean ledStateExpected ) {
        System.out.println("testDistance " + DomainSystemConfig.sonarMockStartDist );
        done = false;
        ActionFunction endFun = (n) -> {
            System.out.println(n);
            this.setDone(true); // per girare intorno al requisito di final
        };

        //tre volte per assicurarsi che sia stato aggiornato il valore nel thread sonar
        sysPc.getController().start( endFun, 2 );

        while(!done) {
            BasicUtils.delay(100); //give time to see ...
        }

        boolean ledStatePc = sysPc.getLed().getState();
        boolean ledStateRasPi = sysRasPi.getLed().getState();

        ColorsOut.out("ledStatePc=" + ledStatePc + " ledStateExpected=" + ledStateExpected, ColorsOut.MAGENTA);
        ColorsOut.out("ledStateRaspi=" + ledStateRasPi + " ledStateExpected=" + ledStateExpected, ColorsOut.MAGENTA);
        assertTrue(ledStatePc == ledStateExpected);
        assertTrue(ledStateRasPi == ledStateExpected);
    }
}
