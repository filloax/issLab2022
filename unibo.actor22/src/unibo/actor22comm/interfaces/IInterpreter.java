package unibo.actor22comm.interfaces;

public interface IInterpreter {
    String elaborateRequest(String request) throws UnknownCommandException;

    void elaborateCommand(String command) throws UnknownCommandException;
}
