package it.unibo.radarSystem22.sprint3.interpreters;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

public class LedApplInterpreter implements IApplInterpreter {
    private ILed led;

    public LedApplInterpreter(ILed led) {
        this.led = led;
    }

    @Override
    public String elaborate(String message) {
        switch(message) {
            case "getState":
                return Boolean.toString(led.getState());
            case "on":
                led.turnOn();
                break;
            case "off":
                led.turnOff();
                break;
            default:
                throw new UknownCommandException("Unknown Led command: " + message);
        }
        return "done: " + message;
    }
}
