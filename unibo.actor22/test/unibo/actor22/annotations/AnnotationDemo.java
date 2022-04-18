package unibo.actor22.annotations;

import unibo.actor22.Qak22Context;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@RemoteContext(name = "raspi", host = "locahost", port = "8080")
@Actor(name = "raspi", local = false, remoteContextName = "raspi")
public class AnnotationDemo {
    public AnnotationDemo() {
        CommSystemConfig.tracing = true;
        AnnotUtil.handleRepeatableActorDeclaration(this);
    }

    public static void main(String[] args) {
        new AnnotationDemo();
    }
}
