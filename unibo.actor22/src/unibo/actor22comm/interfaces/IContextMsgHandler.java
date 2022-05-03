package unibo.actor22comm.interfaces;

public interface IContextMsgHandler extends IApplMsgHandler {
    void addComponent(String name, IApplMsgHandler h);

    void removeComponent(String name);

    IApplMsgHandler getHandler(String name);
}
