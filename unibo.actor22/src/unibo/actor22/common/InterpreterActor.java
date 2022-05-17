package unibo.actor22.common;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import org.jetbrains.annotations.NotNull;
import unibo.actor22.QakActor22;
import unibo.actor22comm.interfaces.IInterpreter;
import unibo.actor22comm.interfaces.UnknownCommandException;
import unibo.actor22comm.utils.ColorsOut;

public class InterpreterActor extends QakActor22 {
    private final IInterpreter interpreter;

    public InterpreterActor(@NotNull String name, IInterpreter interpreter) {
        super(name);
        this.interpreter = interpreter;
    }

    @Override
    protected void handleMsg(IApplMessage msg) {
        try {
            if (msg.isRequest()) {
                String answer = interpreter.elaborateRequest(msg.msgContent());
                IApplMessage reply = MsgUtil.buildReply(
                        getName(),
                        msg.msgId(),
                        answer,
                        msg.msgSender()
                );
                ColorsOut.out(getName() + " | reply= " + reply, ColorsOut.CYAN);
                sendReply(msg, reply);
            } else {
                interpreter.elaborateCommand(msg.msgContent());
            }
        } catch (UnknownCommandException e) {
            e.printStackTrace();
        }
    }
}
