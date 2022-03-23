package it.unibo.radarSystem22.sprint1;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Sprint1Test {
    private RadarSystemSprint1Main sys;

    @Before
    public void setUp() {
        System.out.println("setUp");
        try {
            sys = new RadarSystemSprint1Main();
            sys.setup( null,null );
            sys.configure();
            DomainSystemConfig.testing    		= true;
            DomainSystemConfig.tracing    		= true;
            RadarSystemConfig.sonarObservable   = false; // no observable, meglio per 1shot
        } catch (Exception e) {
            fail("setup ERROR " + e.getMessage() );
        }
    }

    @After
    public void endTest() {
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
        // IRadarDisplay radar = RadarDisplay.getRadarDisplay();
        done = false;
        ActionFunction endFun = (n) -> {
            System.out.println(n);
            this.setDone(true); // per girare intorno al requisito di final
        };

        sys.getController().start( endFun, 2 ); // più volte anche se 1shot per assicurarsi aggiornamento distanza

        while(!done) {
            BasicUtils.delay(100); //give time to see ...
        }

        boolean ledState = sys.getLed().getState();
        //int radarDisplayedDistance = radar.getCurDistance();

        ColorsOut.out("ledState=" + ledState + " ledStateExpected=" + ledStateExpected, ColorsOut.MAGENTA);
//            ColorsOut.out("ledState=" + ledState + " ledStateExpected=" + ledStateExpected + " radarDisplayedDistance=" + radarDisplayedDistance, ColorsOut.MAGENTA);
//            assertTrue(  ledState == ledStateExpected
//                    && radarDisplayedDistance == DomainSystemConfig.testingDistance);
        assertTrue(ledState == ledStateExpected);
    }
}
