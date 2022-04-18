package it.unibo.radarSystemActor.interpreter;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystemActor.common.ApplData;
import unibo.actor22comm.interfaces.IInterpreter;
import unibo.actor22comm.interfaces.UnknownCommandException;


public class LedApplInterpreter implements IInterpreter {
	private ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}

	@Override
 	public void elaborateCommand(String command) throws UnknownCommandException {
		switch (command) {
			case ApplData.cmdLedOn:
				led.turnOn();
				break;
			case ApplData.cmdLedOff:
				led.turnOff();
				break;
			default:
				throw new UnknownCommandException(command);
		}
 	}

	@Override
 	public String elaborateRequest(String request) throws UnknownCommandException {
 	    String answer;
		switch (request) {
			case ApplData.reqLedState:
				answer = Boolean.toString(led.getState());
				break;
			default:
				throw new UnknownCommandException(request);
		}
        return answer;
 	}
}