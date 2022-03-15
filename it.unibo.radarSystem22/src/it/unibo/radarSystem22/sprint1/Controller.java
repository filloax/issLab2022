package it.unibo.radarSystem22.sprint1;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
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
        sonar.activate();
        activate(limit);
    }

    protected void activate( int limit ) {
        new Thread() {
            public void run() {
                sonar.activate();
                if( sonar.isActive() ) {
                    for(int i = 1; i <= limit; i++) { //meglio per il testing ...
                        IDistance d = sonar.getDistance();
                        if (radar != null)
                            RadarGuiUsecase.doUseCase(radar, d);
                        LedAlarmUsecase.doUseCase(led,  d );
                    }
                }
                sonar.deactivate();
                endFun.run("Controller | BYE ");  //CALLBACK
            }
        }.start();
    }
}
