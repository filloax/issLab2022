package unibo.actor22comm.interfaces;

import it.unibo.kactor.IApplMessage;

public interface IApplInterpreter {
    String elaborate(IApplMessage message);

    String elaborate(String message);
}
