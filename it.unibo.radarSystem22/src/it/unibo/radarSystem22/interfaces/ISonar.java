package it.unibo.radarSystem22.interfaces;

public interface ISonar {
    void activate();
    void deactivate();
    IDistance getDistance();
    boolean isActive();
}
