package it.unibo.radarSystem22.domain.mock;

import it.unibo.radarSystem22.domain.interfaces.ILed;

public class LedMock implements ILed {
    private boolean state;

    public LedMock() {
        state = false;
    }

    public void turnOn() {
        state = true;
    }

    public void turnOff() {
        state = false;
    }

    public boolean getState() {
        return state;
    }
}