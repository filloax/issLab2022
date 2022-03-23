package it.unibo.radarSystem22.sprint1;

import it.unibo.radarSystem22.domain.SonarObserverLambda;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.usecases.LedAlarmUsecase;
import it.unibo.radarSystem22.sprint1.usecases.RadarGuiUsecase;

public class Controller {
    private ILed led;
    private ISonar sonar;
    private IRadarDisplay radar;

    private ActionFunction endFun;

    private Controller(ILed led, ISonar sonar, IRadarDisplay radar) {
        this.led = led;
        this.sonar = sonar;
        this.radar = radar;
    }

    public static Controller create(ILed led, ISonar sonar, IRadarDisplay radar) {
        return new Controller(led, sonar, radar);
    }

    public void start( ActionFunction endFun, int limit) {
        this.endFun = endFun;
        activate(limit);
    }

    protected void activate( int limit ) {
        sonar.activate();
        if (RadarSystemConfig.sonarObservable) {
            if (!(sonar instanceof ISonarObservable))
                throw new IllegalArgumentException("Controller richiede SonarObservable con cfg.sonarObservable=true");
            ISonarObservable sonarObservable = (ISonarObservable) sonar;
            sonarObservable.subscribe(SonarObserverLambda.make()
                    .setUpdate(d -> {
                        if (radar != null)
                            RadarGuiUsecase.doUseCase(radar, d);
                        LedAlarmUsecase.doUseCase(led,  d );
                    })
                    .setDeactivated(() -> { endFun.run("Controller | BYE "); })
            );
            BasicUtils.delay(limit * DomainSystemConfig.sonarDelay);
            sonar.deactivate();
        } else {
            new Thread() {
                public void run() {
                    for (int i = 1; i <= limit; i++) { //meglio per il testing ...
                        IDistance d = sonar.getDistance();
                        if (radar != null)
                            RadarGuiUsecase.doUseCase(radar, d);
                        LedAlarmUsecase.doUseCase(led,  d );
                        BasicUtils.delay(DomainSystemConfig.sonarDelay);
                    }
                    sonar.deactivate();
                    endFun.run("Controller | BYE ");  //CALLBACK
                }
            }.start();
        }
    }
}
