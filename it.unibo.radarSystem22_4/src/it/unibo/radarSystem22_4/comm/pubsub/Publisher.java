package it.unibo.radarSystem22_4.comm.pubsub;

import alice.tuprolog.Int;
import it.unibo.radarSystem22_4.comm.ApplMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.Interaction2021;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Attualmente non gestisce il termine di una connessione
 */
public class Publisher extends ApplMsgHandler {
    private Map<String, Interaction2021> subscribers;

    public static Publisher create(String name) {
        return new Publisher(name);
    }

    public Publisher(String name) {
        super(name);
        subscribers = new HashMap<>();
    }

    public void publish(String payload) {
        for (String subscriberTarget : subscribers.keySet()) {
            Interaction2021 conn = subscribers.get(subscriberTarget);
            IApplMessage msg = CommUtils.buildDispatch(getName(), "pub", payload, subscriberTarget);
            sendMsgToClient(msg.toString(), conn);
        }
    }

    private void subscribe(String subscriberName, Interaction2021 conn) {
        subscribers.put(subscriberName, conn);
    }

    private void unsubscribe(String subscriberName) {
        subscribers.remove(subscriberName);
    }

    @Override
    public void elaborate(IApplMessage message, Interaction2021 conn) {
        if (!message.msgId().equals("sub"))
            throw new IllegalArgumentException("Unrecognized message id: " + message.msgId());

        switch (message.msgContent()) {
            case "subscribe":
                subscribe(message.msgSender(), conn);
                break;
            case "unsubscribe":
                unsubscribe(message.msgSender());
                break;
            default:
                throw new IllegalArgumentException("Unrecognized message payload: " + message.msgContent());
        }
    }
}
