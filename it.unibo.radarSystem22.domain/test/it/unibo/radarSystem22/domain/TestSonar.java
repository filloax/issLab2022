package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import org.junit.Before;
import org.junit.Test;

public class TestSonar {

    @Before
    public void up() {
        DomainSystemConfig.setTheConfiguration();
    }

    @Test
    public void testSonar() {
        int maxDelta = 1;

        if (DomainSystemConfig.simulation) {
            ColorsOut.outappl("Running sonar test in SIMULATION mode", ColorsOut.ANSI_PURPLE);
            maxDelta = 1;
            DomainSystemConfig.sonarMockDelta = -1;
        } else {
            ColorsOut.outappl("Running sonar test in REAL mode", ColorsOut.ANSI_PURPLE);
        }

        ISonar sonar = DeviceFactory.createSonar();
        new SonarTestConsumer(sonar, maxDelta).start();
        sonar.activate();
        BasicUtils.delay(100);
        while (sonar.isActive()) {
            BasicUtils.delay(100);
        }
    }
}
