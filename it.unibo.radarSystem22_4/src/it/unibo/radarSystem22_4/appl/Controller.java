package it.unibo.radarSystem22_4.appl;

import it.unibo.radarSystem22.domain.SonarObserverLambda;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22_4.appl.usecases.*;
import it.unibo.radarSystem22.domain.DeviceFactory;

import java.time.LocalTime;

public class Controller {
private ILed led;
private ISonar sonar;
private IRadarDisplay radar;
private ActionFunction endFun;

	public static Controller create(ILed led, ISonar sonar,IRadarDisplay radar ) {
		return new Controller( led,  sonar, radar  );
	}
	public static Controller create(ILed led, ISonar sonar ) {
		return new Controller( led,  sonar, DeviceFactory.createRadarDisplay()  );
	}
	
	private Controller( ILed led, ISonar sonar,IRadarDisplay radar ) {
		this.led    = led;
		this.sonar  = sonar;
		this.radar  = radar;
  		led.turnOff();
	}
		
	public void start( ActionFunction endFun, int limit ) {
		this.endFun = endFun;
		ColorsOut.outappl("Controller | start with endFun=" + endFun , ColorsOut.GREEN);
		sonar.activate();
		activate( limit );
	}
	

	/*
	 * Il Controller riceve dati dal sonar e attiva gli use cases
	 */
	protected void activate( int limit ) {
		if (RadarSystemConfig.useSonarObservable) {
			if (!(sonar instanceof ISonarObservable))
				throw new IllegalStateException("Sonar needs to be observable with useSonarObservable=true");

			ISonarObservable sonarObservable = (ISonarObservable) sonar;

			sonarObservable.subscribe(SonarObserverLambda.make()
					.setUpdate(d -> {
						String timestamp = LocalTime.now().toString();
						ColorsOut.outappl("Controller | d=" + d + " at " + timestamp, ColorsOut.GREEN);
						if (radar != null)
							RadarGuiUsecase.doUseCase(radar, d);    //
						LedAlarmUsecase.doUseCase(led, d);
					})
					.setDeactivated(() -> {
						endFun.run("Controller | <Observer> BYE ");
					})
			);

			BasicUtils.aboutThreads("Controller activated | ");

			// Limite di tempo equivalente
			BasicUtils.delay(DomainSystemConfig.sonarDelay * limit);
			sonarObservable.deactivate();
		} else {
			new Thread() {
				public void run() {
					BasicUtils.aboutThreads("Controller activated | ");
					try {
						boolean sonarActive = sonar.isActive();
						if (sonarActive) {
							for (int i = 1; i <= limit; i++) {
								//while( sonar.isActive() ) {
								IDistance d = sonar.getDistance(); //potrebbe essere bloccante
								ColorsOut.outappl("Controller | d=" + d + " i=" + i, ColorsOut.GREEN);
								if (radar != null) RadarGuiUsecase.doUseCase(radar, d);    //
								LedAlarmUsecase.doUseCase(led, d);
								BasicUtils.delay(DomainSystemConfig.sonarDelay);  //Al ritmo della generazione ...
							}
						}
						//ColorsOut.outappl("Controller | BYE", ColorsOut.BLUE  );
						sonar.deactivate();
						endFun.run("Controller | BYE ");
						//System.exit(0);
					} catch (Exception e) {
						ColorsOut.outerr("ERROR" + e.getMessage());
					}
				}
			}.start();
		}
	}
 }
