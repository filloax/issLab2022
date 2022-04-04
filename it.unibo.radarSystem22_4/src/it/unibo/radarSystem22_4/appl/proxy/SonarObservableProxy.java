package it.unibo.radarSystem22_4.appl.proxy;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.SafeUpdateSet;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.ISubMsgHandler;
import it.unibo.radarSystem22_4.comm.pubsub.Subscriber;
import it.unibo.radarSystem22_4.comm.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;

public class SonarObservableProxy implements ISonarObservable {
    private Subscriber sonarSubscriber;
    private SafeUpdateSet<ISonarObserver> observers;
    private IDistance lastDistance;
    private boolean lastActivated; // non in grado di chiedere valore attuale; limitazione subscriber per ora
    private String name;

    public IApplMessage sonarActivate;
    public IApplMessage sonarDeactivate;

    public SonarObservableProxy(String name, String host, String entry, ProtocolType protocol) {
        this.name = name;

        // Richiede SonarEnabler separato da publisher attivo su contesto
        sonarActivate   = CommUtils.buildDispatch(name, "cmd", "activate",   "sonar");
        sonarDeactivate = CommUtils.buildDispatch(name, "cmd", "deactivate", "sonar");

        observers = new SafeUpdateSet<>();
        lastDistance = new Distance(DomainSystemConfig.sonarDistanceMax);
        lastActivated = false;

        ISubMsgHandler subHandler = new SonarSubMsgHandler(this);
        sonarSubscriber = new Subscriber(name, host, entry, protocol, subHandler);

        sonarSubscriber.subscribe("sonarPub");
    }

    private void onMessage(IApplMessage message) {
        switch (message.msgId()) {
            case "up":
                onActivated(message);
                break;
            case "distance":
                onDistance(message);
                break;
            case "down":
                onDeactivated(message);
                break;
            default:
                ColorsOut.out(String.format("sonarObsProxy %s: unknown message id %s", name, message.msgId()), ColorsOut.YELLOW);
        }
    }

    private void onActivated(IApplMessage message) {
        observers.forEach(ISonarObserver::activated);
    }

    private void onDistance(IApplMessage message) {
        String distStr = message.msgContent();
        IDistance dist = new Distance(Integer.parseInt(distStr));
        lastDistance = dist;
        observers.forEach(obs -> obs.update(dist));
    }

    private void onDeactivated(IApplMessage message) {
        observers.forEach(ISonarObserver::deactivated);
    }

    @Override
    public void subscribe(ISonarObserver sonarObserver) {
        observers.add(sonarObserver);
    }

    @Override
    public void unsubscribe(ISonarObserver sonarObserver) {
        observers.remove(sonarObserver);
    }

    @Override
    public void activate() {
        lastActivated = true;
        sonarSubscriber.sendCommandOnConnection(sonarActivate.toString());
    }

    @Override
    public void deactivate() {
        lastActivated = false;
        sonarSubscriber.sendCommandOnConnection(sonarDeactivate.toString());
    }

    @Override
    // Richieste non possibili: usa ultima distanza ricevuta
    public IDistance getDistance() {
        return lastDistance;
    }

    @Override
    // Richieste non possibili: usa ultima chiamata
    public boolean isActive() {
        return lastActivated;
    }

    private class SonarSubMsgHandler implements ISubMsgHandler {
        private SonarObservableProxy parent;

        public SonarSubMsgHandler(SonarObservableProxy parent) {
            this.parent = parent;
        }

        @Override
        public void elaborate(IApplMessage message) {
            parent.onMessage(message);
        }
    }
}
