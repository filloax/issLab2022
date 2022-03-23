package it.unibo.radarSystem22.sprint2a.proxy;

import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.enablers.EnablerAsServer;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.SafeUpdateSet;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class SonarObserverServer implements ISonarObservable, ISonar {
    private Distance distance;
    private SafeUpdateSet<ISonarObserver> observers;
    private EnablerAsServer serverManager;
    // inglobato per delegare attivazione/disattivazione remota
    private SonarProxyAsClient sonarProxyClient;

    public SonarObserverServer(String name, int port, SonarProxyAsClient sonarProxyClient) {
        this(name, port, ProtocolType.tcp, sonarProxyClient);
    }
    public SonarObserverServer(String name, int port, ProtocolType protocol, SonarProxyAsClient sonarProxyClient) {
        this.distance = new Distance(DomainSystemConfig.sonarDistanceMax);
        this.observers = new SafeUpdateSet<>();
        this.serverManager = createServer(name, port, protocol);
        this.sonarProxyClient = sonarProxyClient;
    }

    protected EnablerAsServer createServer(String name, int port, ProtocolType protocol) {
        return new EnablerAsServer(name, port, protocol, new ApplMsgHandler("ProxySonarObservable") {
            @Override
            public void elaborate(String msg, Interaction2021 interaction) {
                int distVal;
                try {
                    distVal = Integer.parseInt(msg);
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return;
                }
                updateDistance(new Distance(distVal));
            }
        });
    }

    protected void updateDistance(Distance newDistance) {
        distance = newDistance;
        observers.forEach(obs -> obs.update(newDistance));
    }

    @Override
    public void activate() {
        serverManager.start();
        sonarProxyClient.activate(); // attiva sonar remoto
        observers.forEach(obs -> obs.activated());
    }

    // TODO: attivarlo quando il server TCP si spegne, processo di controllo o boh
    @Override
    public void deactivate() {
        serverManager.stop();
        sonarProxyClient.deactivate(); // disattiva sonar remoto
        observers.forEach(obs -> obs.deactivated());
    }

    @Override
    public IDistance getDistance() {
        return distance;
    }

    @Override
    public boolean isActive() {
        return serverManager.isActive();
    }

    @Override
    public void subscribe(ISonarObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ISonarObserver observer) {
        observers.remove(observer);
    }
}
