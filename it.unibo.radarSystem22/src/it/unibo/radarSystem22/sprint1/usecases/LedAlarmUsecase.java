package it.unibo.radarSystem22.sprint1.usecases;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class LedAlarmUsecase {
    public static void doUseCase(ILed led, IDistance d) {
        if (d.getVal() < DomainSystemConfig.DLIMIT) {
            led.turnOn();
        } else {
            led.turnOff();
        }
    }
}
