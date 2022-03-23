package it.unibo.radarSystem22.sprint2a.proxy;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.radarSystem22.domain.SonarObserverLambda;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;

public class SonarObserverClient extends ProxyAsClient {
    private ISonarObservable sonar;
    private IDistance lastDistance = null;

    public SonarObserverClient(String name, String host, String entry, ISonarObservable sonar) {
        this(name, host, entry, ProtocolType.tcp, sonar);
    }
    public SonarObserverClient(String name, String host, String entry, ProtocolType protocol, ISonarObservable sonar) {
        super(name, host, entry, protocol);
        this.sonar = sonar;
        this.sonar.subscribe(SonarObserverLambda.make()
                .setUpdate((IDistance dist) -> {
                    if (lastDistance == null || lastDistance.getVal() != dist.getVal()) {
                        updateLastDistance(dist);
                        sendCommandOnConnection(Integer.toString(dist.getVal()));
                    }
                })
                .setDeactivated(() -> {
                    close();
                })
        );
    }

    protected void updateLastDistance(IDistance dist) {
        lastDistance = dist;
    }
}
