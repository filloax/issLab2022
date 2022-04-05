package unibo.actor22.common;
 
import org.jetbrains.annotations.NotNull;
import unibo.actor22.interpreter.LedApplInterpreter;
import it.unibo.radarSystem22.domain.DeviceFactory;


/*
 * Funge da interprete di comandi e richieste
 */
public class LedActor extends InterpreterActor {
	public LedActor(@NotNull String name) {
		super(name, new LedApplInterpreter(DeviceFactory.createLed()));
	}
}
