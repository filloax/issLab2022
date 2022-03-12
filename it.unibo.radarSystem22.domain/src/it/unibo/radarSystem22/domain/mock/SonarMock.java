package it.unibo.radarSystem22.domain.mock;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class SonarMock extends SonarModel {
    private int distUpdateDelta = -1;

    @Override
    protected void sonarSetUp() {
        curVal = new Distance(DomainSystemConfig.sonarMockStartDist);
        distUpdateDelta = DomainSystemConfig.sonarMockDelta;
        ColorsOut.outappl("[Sonar mock] set up", ColorsOut.CYAN);
    }

    @Override
    protected void sonarProduce() {
        if (DomainSystemConfig.testing ) {
            ColorsOut.outappl("[Sonar mock] testing oneshot mode", ColorsOut.MAGENTA);
            updateDistance(DomainSystemConfig.testingDistance);
            stopped = true;
        } else {
            int val = getDistance().getVal() + distUpdateDelta;
            updateDistance(val);
            if (DomainSystemConfig.sonarMockVerbose)
                ColorsOut.out("\tCurrent distance: " + val);
            stopped = val <= 0 || val >= DomainSystemConfig.sonarDistanceMax;
            if (stopped && DomainSystemConfig.sonarMockVerbose)
                ColorsOut.outappl("Stopped", ColorsOut.GREEN);
            BasicUtils.delay(DomainSystemConfig.sonarDelay);
        }
    }
}
