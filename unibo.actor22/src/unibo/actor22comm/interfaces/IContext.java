package unibo.actor22comm.interfaces;

public interface IContext {
    //	public void addComponent( String name, IApplMsgHandler h);
//	public void removeComponent( String name );
    void activate();

    void deactivate();
}
