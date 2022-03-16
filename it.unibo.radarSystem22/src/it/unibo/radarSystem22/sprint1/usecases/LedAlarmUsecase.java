package it.unibo.radarSystem22.sprint1.usecases;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;

public class LedAlarmUsecase {
    public static void doUseCase(ILed led, IDistance d) {
        if (d.getVal() < RadarSystemConfig.DLIMIT) {
            led.turnOn();
        } else {
            led.turnOff();
        }
    }
}
