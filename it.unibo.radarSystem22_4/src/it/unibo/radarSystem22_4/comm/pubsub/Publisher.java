package it.unibo.radarSystem22_4.comm.pubsub;

import alice.tuprolog.Int;
import it.unibo.radarSystem22_4.comm.ApplMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.Interaction2021;
import it.unibo.radarSystem22_4.comm.utils.ColorsOut;
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

    public void publish(String id, String payload) {
        for (String subscriberTarget : subscribers.keySet()) {
            Interaction2021 conn = subscribers.get(subscriberTarget);
            IApplMessage msg = CommUtils.buildDispatch(getName(), id, payload, subscriberTarget);
            sendMsgToClient(msg.toString(), conn);
        }
        ColorsOut.out(String.format("Pub [%s]: published '%s'", name, payload), ColorsOut.MAGENTA);
    }

    private void addSubscriber(String subscriberName, Interaction2021 conn) {
        ColorsOut.out(String.format("Pub [%s]: sub %s subscribed", name, subscriberName), ColorsOut.MAGENTA);
        subscribers.put(subscriberName, conn);
    }

    private void removeSubscriber(String subscriberName) {
        ColorsOut.out(String.format("Pub [%s]: sub %s unsubscribed", name, subscriberName), ColorsOut.MAGENTA);
        subscribers.remove(subscriberName);
    }

    @Override
    public void elaborate(IApplMessage message, Interaction2021 conn) {
        if (!message.msgId().equals("sub"))
            throw new IllegalArgumentException("Unrecognized message id: " + message.msgId());

        switch (message.msgContent()) {
            case "subscribe":
                addSubscriber(message.msgSender(), conn);
                break;
            case "unsubscribe":
                removeSubscriber(message.msgSender());
                break;
            default:
                throw new IllegalArgumentException("Unrecognized message payload: " + message.msgContent());
        }
    }
}
