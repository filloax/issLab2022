package it.unibo.radarSystem22_4.comm.interfaces;

import it.unibo.radarSystem22_4.comm.UnknownCommandException;

public interface ISubMsgHandler {
    void elaborate(IApplMessage message);
}
