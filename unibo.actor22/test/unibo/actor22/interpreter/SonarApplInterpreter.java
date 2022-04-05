package unibo.actor22.interpreter;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import unibo.actor22.common.ApplData;
import unibo.actor22comm.interfaces.IApplInterpreter;
import unibo.actor22comm.interfaces.UnknownCommandException;


public class SonarApplInterpreter implements IApplInterpreter {
	private ISonar sonar;

	public SonarApplInterpreter(ISonar sonar) {
 		this.sonar = sonar;
	}

	@Override
 	public void elaborateCommand(String command) throws UnknownCommandException {
		switch (command) {
			case ApplData.cmdSonarActivate:
				sonar.activate();
				break;
			case ApplData.cmdSonarDeactivate:
				sonar.deactivate();
				break;
			default:
				throw new UnknownCommandException(command);
		}
 	}

	@Override
 	public String elaborateRequest(String request) throws UnknownCommandException {
 	    String answer;
		switch (request) {
			case ApplData.reqGetDistance:
				answer = Integer.toString(sonar.getDistance().getVal());
				break;
			case ApplData.reqIsActive:
				answer = Boolean.toString(sonar.isActive());
				break;
			default:
				throw new UnknownCommandException(request);
		}
        return answer;
 	}
}