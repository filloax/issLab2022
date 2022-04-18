package it.unibo.radarSystemActor.common;

import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystemActor.interpreter.SonarApplInterpreter;
import org.jetbrains.annotations.NotNull;
import unibo.actor22.common.InterpreterActor;


/*
 * Funge da interprete di comandi e richieste
 */
public class SonarActor extends InterpreterActor {
	public SonarActor(@NotNull String name) {
		super(name, new SonarApplInterpreter(DeviceFactory.createSonar()));
	}
}
