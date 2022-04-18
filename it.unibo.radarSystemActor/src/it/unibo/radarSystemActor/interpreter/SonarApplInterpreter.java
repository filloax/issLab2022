package it.unibo.radarSystemActor.interpreter;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystemActor.common.ApplData;
import unibo.actor22comm.interfaces.IInterpreter;
import unibo.actor22comm.interfaces.UnknownCommandException;


public class SonarApplInterpreter implements IInterpreter {
	private ISonar sonar;

	public SonarApplInterpreter(ISonar sonar) {
 		this.sonar = sonar;
	}

	@Override
 	public void elaborateCommand(String command) throws UnknownCommandException {
		switch (command) {
			case ApplData.cmdActivate:
				sonar.activate();
				break;
			case ApplData.cmdDeactivate:
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
			case ApplData.reqDistance:
				answer = Integer.toString(sonar.getDistance().getVal());
				break;
			case ApplData.reqSonarActive:
				answer = Boolean.toString(sonar.isActive());
				break;
			default:
				throw new UnknownCommandException(request);
		}
        return answer;
 	}
}