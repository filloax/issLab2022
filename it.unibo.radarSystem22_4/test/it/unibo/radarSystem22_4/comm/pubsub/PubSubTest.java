package it.unibo.radarSystem22_4.comm.pubsub;

import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.context.ContextMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.ISubMsgHandler;
import it.unibo.radarSystem22_4.comm.udp.UdpServer;
import it.unibo.radarSystem22_4.comm.utils.BasicUtils;
import it.unibo.radarSystem22_4.comm.utils.CommSystemConfig;
import it.unibo.radarSystem22_4.common.NaiveApplHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PubSubTest {
    private UdpServer server;
    private ContextMsgHandler context;
    public static final int testPort = 8900;

    // Workaround per requisito variabili final in lambda Java
    private Map<String, Integer> testState;

    @Before
    public void init() {
        CommSystemConfig.tracing = true;
        testState = new HashMap<>();
    }

    protected void startTheServer() {
        context = new ContextMsgHandler("testContext");
        server = new UdpServer("testContextServer", testPort, context);
        server.activate();
    }

    protected void stopTheServer() {
        if( server != null ) server.deactivate();
    }

    @Test
    public void basicTest() {
        startTheServer();

        String pubName = "testPub";

        Publisher testPub = new Publisher(pubName);
        context.addComponent(pubName, testPub);

        String payload = "testBroadcast";
        int numSubs = 3;

        for (int i = 0; i < numSubs; i++)
            testState.put("basicTest_Completed_" + i, 0);

        for (int i = 0; i < numSubs; i++) {
            final int local_i = i;
            new Thread() {
                public void run() {
                    Subscriber sub = new Subscriber(
                            "testSub" + local_i,
                            "localhost",
                            Integer.toString(testPort),
                            ProtocolType.udp,
                            new ISubMsgHandler() {
                                @Override
                                public void elaborate(IApplMessage message) {
                                    if (message.msgContent().equals(payload)) {
                                        testState.put("basicTest_Completed_" + local_i, 1);
                                        System.out.println(local_i + ": received");
                                    }
                                }
                            }
                    );
                    sub.subscribe(pubName);
                    System.out.println(local_i + ": subscribed");
                }
            }.start();
        }

        BasicUtils.delay(300);

        testPub.publish("test", payload);

        BasicUtils.delay(300);

        stopTheServer();

        int numCompleted = 0;
        for (int i = 0; i < numSubs; i++)
            numCompleted += testState.get("basicTest_Completed_" + i);

        assertEquals(numSubs, numCompleted);
    }
}
