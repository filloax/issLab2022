package unibo.actor22.interpreter;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import unibo.actor22.common.ApplData;
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
			case ApplData.cmdLedon:
				led.turnOn();
				break;
			case ApplData.cmdLedoff:
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