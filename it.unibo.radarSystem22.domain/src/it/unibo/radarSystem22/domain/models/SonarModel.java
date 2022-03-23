package it.unibo.radarSystem22.domain.models;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcrete;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class SonarModel implements ISonar, ISonarObservable {
    protected boolean stopped = true; //se true il sonar si ferma
    protected IDistance curVal = new Distance(90);
    protected Set<ISonarObserver> observers;
    private boolean runningObserversUpdates = false;
    private Set<ISonarObserver> toAddAfterUpdates;
    private Set<ISonarObserver> toRemoveAfterUpdates;

    protected SonarModel() {
        observers = new HashSet<>();
        toAddAfterUpdates = new HashSet<>();
        toRemoveAfterUpdates = new HashSet<>();

        sonarSetUp();
    }

    public static ISonar create() {
        if (DomainSystemConfig.simulation || DomainSystemConfig.simulateSonar) {
            return createSonarMock();
        } else {
            return createSonarConcrete();
        }
    }
    public static ISonarObservable createObservable() {
        return (ISonarObservable) create();
    }

    public static ISonar createSonarMock() {
        return new SonarMock();
    }
    public static ISonar createSonarConcrete() {
        return new SonarConcrete();
    }

    protected abstract void sonarSetUp();
    protected abstract void sonarProduce(); // invia echo del sonar, modifica curVal

    protected void updateDistance(IDistance dist) {
        curVal = dist;
        if (DomainSystemConfig.sonarVerbose)
            ColorsOut.out("\tCurrent distance: " + curVal.getVal());
    }
    protected void updateDistance(int val) { updateDistance(new Distance(val)); }

    @Override
    public void activate() {
        if (!stopped)
            throw new RuntimeException("Sonar già attivato!");
        stopped = false;
        safeObserversForEach(obs -> obs.activated());
        new Thread(() -> {
            while (!stopped) {
                sonarProduce();
                safeObserversForEach(obs -> obs.update(curVal));
            }
            safeObserversForEach(obs -> obs.deactivated());
        }).start();
    }

    @Override
    public void deactivate() {
        if (!stopped) {
            stopped = true;
            if (DomainSystemConfig.sonarVerbose)
                ColorsOut.out("\tDeactivated sonar", ColorsOut.YELLOW);
        }
    }

    private void safeObserversForEach(Consumer<ISonarObserver> func) {
        // Per permettere esecuzione di subscribe/unsubscribe
        // dentro a ISonarObserver.update,activate,ecc. senza deadlock
        runningObserversUpdates = true;
        synchronized (observers) {
            observers.forEach(func);
            runningObserversUpdates = false;
            // Synchronized non necessario, visto che
            // runningObserverUpdates è false
            // e tutte le modifiche ai due set temporanei
            // sono eseguite con esso a true
            // (e sono privati, quindi nessuna sottoclasse
            // potrebbe interferire
            toAddAfterUpdates.forEach(obs -> observers.add(obs));
            toRemoveAfterUpdates.forEach(obs -> observers.remove(obs));
            toAddAfterUpdates.clear();
            toRemoveAfterUpdates.clear();
        }
    }

    @Override
    public IDistance getDistance() {
        return curVal;
    }

    @Override
    public boolean isActive() {
        return !stopped;
    }

    @Override
    public void subscribe(ISonarObserver observer) {
        if (runningObserversUpdates) {
            synchronized (toAddAfterUpdates) {
                toAddAfterUpdates.add(observer);
            }
        } else {
            synchronized (observers) {
                observers.add(observer);
            }
        }
    }

    @Override
    public void unsubscribe(ISonarObserver observer) {
        if (runningObserversUpdates) {
            synchronized (toRemoveAfterUpdates) {
                toRemoveAfterUpdates.add(observer);
            }
        } else {
            synchronized (observers) {
                observers.remove(observer);
            }
        }

    }
}
