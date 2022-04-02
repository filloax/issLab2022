package it.unibo.radarSystem22_4.appl.interpreter;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22_4.comm.UnknownCommandException;
import it.unibo.radarSystem22_4.comm.interfaces.IApplInterpreter;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;
 

/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplInterpreter implements IApplInterpreter  {
	private ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}

 	@Override
    public String elaborate( IApplMessage message ) throws UnknownCommandException {
		String answer = null; //no answer
		if(  message.isRequest() ) {
			answer = elabRequest(message);
		}else { //command => no answer
	  		elabCommand(message);
		}
		return answer;
    }   
 	
 	protected void elabCommand( IApplMessage message ) throws UnknownCommandException {
		String payload = message.msgContent();
		switch (payload) {
			case "on":
				led.turnOn();
				break;
			case "off":
				led.turnOff();
				break;
			default:
				throw new UnknownCommandException(payload);
		}
 	}
 	
 	protected String elabRequest( IApplMessage message ) throws UnknownCommandException {
 	    String payload = message.msgContent();
 	    String answer;
		switch (payload) {
			case "getState":
				answer = Boolean.toString(led.getState());
				break;
			default:
				throw new UnknownCommandException(payload);
		}
        IApplMessage reply = CommUtils.prepareReply( message, answer);
        return (reply.toString()); //msg(...)
 	}
}