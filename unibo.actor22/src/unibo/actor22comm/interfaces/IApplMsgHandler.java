package unibo.actor22comm.interfaces;

import it.unibo.kactor.IApplMessage;

public interface IApplMsgHandler {
    String getName();

    //public void elaborate( String message, Interaction2021 conn ) ;
    void elaborate(IApplMessage message, Interaction2021 conn);//ESTENSIONE dopo Context

    void sendMsgToClient(String message, Interaction2021 conn);

    void sendAnswerToClient(String message, Interaction2021 conn);
}
