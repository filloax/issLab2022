package unibo.actor22comm.interfaces;

import it.unibo.is.interfaces.protocols.IConnInteraction;

public interface Interaction2021 extends IConnInteraction {
    void forward(String msg) throws Exception;

    String request(String msg) throws Exception;

    void reply(String reqid) throws Exception;

    String receiveMsg() throws Exception;

    void close() throws Exception;
	/* From IConnInteraction:
	public void sendALine(  String msg ) throws Exception;
	public void sendALine(  String msg, boolean isAnswer ) throws Exception;
	public String receiveALine(  ) throws Exception;
	public void closeConnection( ) throws Exception;
	 */
}
