package it.unibo.radarSystem22.domain.models;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.NotImplementedException;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModel implements ISonar {
    protected boolean stopped = false; //se true il sonar si ferma
    protected IDistance curVal = new Distance(90);

    protected SonarModel() {
        sonarSetUp();
    }

    public static ISonar create() {
        if (DomainSystemConfig.simulation) {
            return createSonarMock();
        } else {
            return createSonarConcrete();
        }
    }

    public static ISonar createSonarMock() {
        return new SonarMock();
    }
    public static ISonar createSonarConcrete() {
        throw new NotImplementedException("Sonar Concrete not implemented yet");
    }

    protected abstract void sonarSetUp();
    protected abstract void sonarProduce(); // invia echo del sonar, modifica curVal

    protected void updateDistance(IDistance dist) {
        curVal = dist;
    }
    protected void updateDistance(int val) { updateDistance(new Distance(val)); }

    @Override
    public void activate() {
        stopped = false;
        new Thread() {
            public void run() {
                while (!stopped) {
                    sonarProduce();
                }
            }
        }.start();
    }

    @Override
    public void deactivate() {
        stopped = true;
    }

    @Override
    public IDistance getDistance() {
        return curVal;
    }

    @Override
    public boolean isActive() {
        return !stopped;
    }
}
