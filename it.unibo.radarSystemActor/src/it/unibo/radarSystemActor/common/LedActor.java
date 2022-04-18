package it.unibo.radarSystemActor.common;
 
import org.jetbrains.annotations.NotNull;
import it.unibo.radarSystemActor.interpreter.LedApplInterpreter;
import it.unibo.radarSystem22.domain.DeviceFactory;
import unibo.actor22.common.InterpreterActor;


/*
 * Funge da interprete di comandi e richieste
 */
public class LedActor extends InterpreterActor {
	public LedActor(@NotNull String name) {
		super(name, new LedApplInterpreter(DeviceFactory.createLed()));
	}
}
