package it.unibo.radarSystem22_4.comm.pubsub;

import it.unibo.radarSystem22_4.comm.ApplMessage;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.ISubMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.Interaction2021;
import it.unibo.radarSystem22_4.comm.proxy.ProxyAsClient;
import it.unibo.radarSystem22_4.comm.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Subscriber che può comunque inviare comandi al contesto
 * di riferimento.
 */
public class Subscriber extends ProxyAsClient {
    Set<String> subscribedTo;
    Thread listenThread;
    boolean listening;
    ISubMsgHandler subHandler;

    /**
     *
     * @param name Name of subscriber
     * @param host Hostname / IP
     * @param entry port
     * @param protocol TCP | UDP
     * @param subHandler Handler for messages recevied from publishers
     */
    public Subscriber(String name, String host, String entry, ProtocolType protocol, ISubMsgHandler subHandler) {
        super(name, host, entry, protocol);
        this.subscribedTo = new HashSet<>();
        this.listenThread = null;
        this.listening = false;
        this.subHandler = subHandler;
    }

    public void subscribe(String targetPublisher) {
        IApplMessage msg = CommUtils.buildDispatch(name,"sub", "subscribe", targetPublisher);
        sendCommandOnConnection(msg.toString());
        subscribedTo.add(targetPublisher);
        startListenIfNot();
        ColorsOut.out(String.format("Sub [%s]: subscribed to %s", name, targetPublisher), ColorsOut.YELLOW);
    }

    public void unsubscribe(String targetPublisher) {
        if (subscribedTo.contains(targetPublisher)) {
            IApplMessage msg = CommUtils.buildDispatch(name,"sub", "unsubscribe", targetPublisher);
            sendCommandOnConnection(msg.toString());
            subscribedTo.remove(targetPublisher);
            ColorsOut.out(String.format("Sub [%s]: unsubscribed from %s", name, targetPublisher), ColorsOut.YELLOW);
        }
    }

    private void startListenIfNot() {
        if (!listening || listenThread == null || !listenThread.isAlive()) {
            listening = true;
            listenThread = new Thread(() -> {
                Interaction2021 conn = getConn();
                while (listening) {
                    try {
                        String msgString = conn.receiveMsg();
                        IApplMessage msg = new ApplMessage(msgString);
                        if (msg.msgId().equals("pub")) {
                            subHandler.elaborate(msg);
                        } else {
                            throw new RuntimeException("Received message from not pub!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            listenThread.start();
            ColorsOut.out(String.format("Sub [%s]: now listening", name), ColorsOut.YELLOW);
        }
    }

    public void stopListening() {
        listening = false;
    }

    // Richieste non supportate, essendo in ascolto per messaggi da iscrizioni
    @Override
    public String sendRequestOnConnection(String msg) {
        throw new RuntimeException("Richieste non supportate da subscriber");
    }
}
