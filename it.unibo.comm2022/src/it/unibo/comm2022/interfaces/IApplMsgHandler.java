package it.unibo.comm2022.interfaces;

public interface IApplMsgHandler  {
	String getName();
	void elaborate( String message, Interaction2021 conn ) ;
//	public void elaborate( IApplMessage message, Interaction2021 conn );//ESTENSIONE dopo Context
	void sendMsgToClient( String message, Interaction2021 conn );
	void sendAnswerToClient( String message, Interaction2021 conn  );
}
