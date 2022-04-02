package it.unibo.radarSystem22_4.comm.interfaces;

import it.unibo.radarSystem22_4.comm.UnknownCommandException;

public interface IApplInterpreter {
 	String elaborate(IApplMessage message) throws UnknownCommandException;
}
