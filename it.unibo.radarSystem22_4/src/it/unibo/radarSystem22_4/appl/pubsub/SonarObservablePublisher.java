package it.unibo.radarSystem22_4.appl.pubsub;

import it.unibo.radarSystem22.domain.SonarObserverLambda;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;
import it.unibo.radarSystem22_4.comm.pubsub.Publisher;

public class SonarObservablePublisher extends Publisher {
    private ISonarObservable sonarObservable;
    private ISonarObserver myObserver;

    public static SonarObservablePublisher create(String name, ISonarObservable sonarObservable) {
        return new SonarObservablePublisher(name, sonarObservable);
    }

    public SonarObservablePublisher(String name, ISonarObservable sonarObservable) {
        super(name);
        this.sonarObservable = sonarObservable;
        this.myObserver = SonarObserverLambda.make()
                .setActivated(() -> this.publish("up", ""))
                .setUpdate(distance -> this.publish("distance", distance.toString()))
                .setDeactivated(() -> this.publish("down", ""))
                ;
    }

    public void enable() {
        sonarObservable.subscribe(myObserver);
    }

    public void disable() {
        sonarObservable.unsubscribe(myObserver);
    }
}
