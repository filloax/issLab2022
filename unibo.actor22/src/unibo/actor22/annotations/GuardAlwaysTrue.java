package unibo.actor22.annotations;


import unibo.actor22comm.interfaces.IGuard;

public class GuardAlwaysTrue implements IGuard {
    @Override
    public boolean eval() {
        //ColorsOut.outappl("GuardAlwaysTrue eval" , ColorsOut.ANSI_YELLOW);
        return true;
    }

}
