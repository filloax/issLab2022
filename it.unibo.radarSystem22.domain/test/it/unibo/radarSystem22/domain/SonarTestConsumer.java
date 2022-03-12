package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

import static org.junit.Assert.*;

public class SonarTestConsumer extends Thread {
    private ISonar sonar;
    private int maxDelta;

    public SonarTestConsumer(ISonar sonar, int maxDelta) {
        this.sonar = sonar;
        this.maxDelta = maxDelta;
    }

    @Override
    public void run() {
        int prevValue = sonar.getDistance().getVal();
        while (sonar.isActive()) {
            BasicUtils.delay(DomainSystemConfig.sonarDelay / 2);
            IDistance dist = sonar.getDistance();
            int curVal = dist.getVal();
            int minExpected = prevValue - maxDelta;
            int maxExpected = prevValue + maxDelta;
            assertTrue(curVal >= minExpected && curVal <= maxExpected);
            prevValue = curVal;
        }
    }
}
