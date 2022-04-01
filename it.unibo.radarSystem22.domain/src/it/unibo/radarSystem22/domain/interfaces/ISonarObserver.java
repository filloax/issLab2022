package it.unibo.radarSystem22.domain.interfaces;

public interface ISonarObserver {
    void update(IDistance dist);
    void activated();
    void deactivated();
}
