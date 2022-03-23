package it.unibo.radarSystem22.domain.interfaces;

import java.util.function.Consumer;

public interface ISonarObserver {
    void update(IDistance dist);
    void activated();
    void deactivated();
}
