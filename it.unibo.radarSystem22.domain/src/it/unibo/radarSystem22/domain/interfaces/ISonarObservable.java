package it.unibo.radarSystem22.domain.interfaces;

public interface ISonarObservable extends ISonar {
    void subscribe(ISonarObserver observer);
    void unsubscribe(ISonarObserver observer);
}
