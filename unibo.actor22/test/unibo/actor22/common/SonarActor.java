package unibo.actor22.common;

import it.unibo.radarSystem22.domain.DeviceFactory;
import org.jetbrains.annotations.NotNull;
import unibo.actor22.interpreter.LedApplInterpreter;
import unibo.actor22.interpreter.SonarApplInterpreter;


/*
 * Funge da interprete di comandi e richieste
 */
public class SonarActor extends InterpreterActor {
	public SonarActor(@NotNull String name) {
		super(name, new SonarApplInterpreter(DeviceFactory.createSonar()));
	}
}
