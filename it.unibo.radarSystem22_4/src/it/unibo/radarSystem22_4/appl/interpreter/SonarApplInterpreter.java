package it.unibo.radarSystem22_4.appl.interpreter;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22_4.comm.UnknownCommandException;
import it.unibo.radarSystem22_4.comm.interfaces.IApplInterpreter;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;
 

public class SonarApplInterpreter implements IApplInterpreter{
private	ISonar sonar;

	public SonarApplInterpreter(ISonar sonar) {
		this.sonar = sonar;
	}

 
	@Override
	public String elaborate(IApplMessage message) throws UnknownCommandException {
		//ColorsOut.out("SonarApplInterpreter | elaborate " + message, ColorsOut.BLUE);
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
			case "activate":
				sonar.activate();
				break;
			case "deactivate":
				sonar.deactivate();
				break;
			default:
				throw new UnknownCommandException(payload);
		}
 	}
 	protected String elabRequest( IApplMessage message ) throws UnknownCommandException {
 		String answer;
		String payload = message.msgContent();
		switch (payload) {
			case "getDistance":
				//ColorsOut.out(name+ " | elaborate getDistance="  , ColorsOut.BLUE);
				answer = sonar.getDistance().toString();
				break;
			case "isActive":
				ColorsOut.out("SonarApplInterpreter | isActive " + sonar.isActive(), ColorsOut.BLUE);
				answer = Boolean.toString(sonar.isActive());
				break;
			default:
				throw new UnknownCommandException(payload);
		}
        IApplMessage reply = CommUtils.prepareReply( message, answer);
		return reply.toString();
	}
}
