package unibo.actor22comm.interfaces;

import it.unibo.kactor.IApplMessage;

public interface IApplInterpreter {
	public String elaborateRequest(String request) throws UnknownCommandException;
	public void elaborateCommand(String command) throws UnknownCommandException;
}
