package unibo.wenvUsage22.annot.cleaner;

import it.unibo.kactor.IApplMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.annot.walker.WsConnWEnvObserver;
import unibo.wenvUsage22.common.VRobotMoves;

import java.util.function.Function;
import java.util.function.Supplier;

public class RobotCleaner extends QakActor22FsmAnnot  {
    private Interaction2021 conn;
    private float timePerUnit; // movimento per fare 1 unità, vedi VRobotMoves
    float positionX, positionY; // relativo a home; Y è direzione colonne
    private String lastActiveState;

    public static final String STATE_INIT = "robotStart";
    public static final String STATE_FORWARD = "moveForward";
    public static final String STATE_WALL_FORWARD = "collidedForward";
    public static final String STATE_BACK = "moveBack";
    public static final String STATE_WALL_BACK = "collidedBack";
    public static final String STATE_END = "endState";
    public static final String STATE_PAUSE = "pause";

    public RobotCleaner(String name) {
        super(name);
        timePerUnit = 300;
        positionX = 0;
        positionY = 0;
        lastActiveState = "";
    }
    
    @State( name = STATE_INIT, initial=true)
    @Transition( state = STATE_WALL_FORWARD , msgId = SystemData.endMoveKoId )
    @Transition( state = STATE_PAUSE , msgId = SystemData.haltSysCmdId )
    @Transition( state = STATE_FORWARD ,  msgId = SystemData.endMoveOkId )
    protected void robotStart( IApplMessage msg ) {
        setLastActiveState(STATE_INIT);

        outInfo(msg + " connecting (blocking all the actors ) ... ");
        conn = WsConnection.create("localhost:8091" );
        outInfo("connected " + conn);
        ((WsConnection)conn).addObserver( new WsConnWEnvObserver(getName()) );
        VRobotMoves.step(getName(),conn);
    }
    
    @State( name = STATE_FORWARD)
    @Transition( state = STATE_WALL_FORWARD , msgId = SystemData.endMoveKoId )
    @Transition( state = STATE_PAUSE , msgId = SystemData.haltSysCmdId )
    @Transition( state = STATE_FORWARD,  msgId = SystemData.endMoveOkId)
    protected void moveForward(IApplMessage msg) {
        setLastActiveState(STATE_FORWARD);

        printPosition();
        VRobotMoves.step(getName(),conn);
        // aggiungi intero step, al limite ridurremo poi la posizione
        addPosition(0, 1);
    }
     
    @State( name = STATE_WALL_FORWARD)
    @Transition( state = STATE_END, msgId = SystemData.endMoveKoId)
    @Transition( state = STATE_PAUSE , msgId = SystemData.haltSysCmdId )
    @Transition( state = STATE_BACK, msgId = SystemData.endMoveOkId)
    protected void collidedForward( IApplMessage msg ) {
        setLastActiveState(STATE_WALL_FORWARD);

        JSONObject data = getMsgData(msg);
        // Aggiusta posizione in base a strada effettivamente percorsa
        addPosition(0, -1 + data.getInt("duration") / timePerUnit);

        printPosition();
        VRobotMoves.turnLeftAndStep(getName(), 300, conn);
        addPosition(1, 0);
    }

    @State( name = STATE_BACK)
    @Transition( state = STATE_WALL_BACK , msgId = SystemData.endMoveKoId )
    @Transition( state = STATE_PAUSE , msgId = SystemData.haltSysCmdId )
    @Transition( state = STATE_BACK,  msgId = SystemData.endMoveOkId)
    protected void moveBack( IApplMessage msg ) {
        setLastActiveState(STATE_BACK);

        printPosition();
        VRobotMoves.step(getName(), conn);
        addPosition(0, -1);
    }

    @State( name = STATE_WALL_BACK)
    @Transition( state = STATE_END , msgId = SystemData.endMoveKoId )
    @Transition( state = STATE_PAUSE , msgId = SystemData.haltSysCmdId )
    @Transition( state = STATE_FORWARD, msgId = SystemData.endMoveOkId)
    protected void collidedBack( IApplMessage msg ) {
        setLastActiveState(STATE_WALL_BACK);

        JSONObject data = getMsgData(msg);
        // Aggiusta posizione in base a strada effettivamente percorsa
        addPosition(0, 1 - data.getInt("duration") / timePerUnit);

        printPosition();
        VRobotMoves.turnRightAndStep(getName(), 300, conn);
        addPosition(1, 0);
    }

    @State(name = STATE_END)
    protected void endState(IApplMessage msg) {
        JSONObject data = getMsgData(msg);
        // Aggiusta posizione in base a strada effettivamente percorsa
        addPosition(-1 + data.getInt("duration") / timePerUnit, 0);
        printPosition();

        System.out.println(getName() + " | finished!");
        System.exit(0);
    }

    @State(name = STATE_PAUSE)
    @Transition(state = STATE_FORWARD, msgId = SystemData.startSysCmdId, guard = GuardPauseForward.class)
    @Transition(state = STATE_BACK, msgId = SystemData.startSysCmdId, guard = GuardPauseBack.class)
    protected void pause(IApplMessage msg) {
        CommUtils.delay(100);
    }

    protected void setLastActiveState(String state) {
        setGuardState(state);
        setLastActiveState(state); // inutilizzato al momento
    }

    protected void updatePosition(float x, float y) {
        positionX = x;
        positionY = y;
    }

    protected void addPosition(float deltaX, float deltaY) {
        positionX = positionX + deltaX;
        positionY = positionY + deltaY;
    }

    protected void printPosition() {
        ColorsOut.out(String.format("%s | position: (%.2f, %.2f)", getName(), positionX, positionY));
    }

    protected JSONObject getMsgData(IApplMessage msg) {
        String jsonString = msg.msgContent().substring(1, msg.msgContent().length() - 1);
        try {
            return new JSONObject(new JSONTokener(jsonString));
        } catch (JSONException e) {
            throw new RuntimeException("Messaggio non è JSON corretto: '" + jsonString + "'", e);
        }
    }

//    @Override
//    protected void handleMsg(IApplMessage msg) {
//         System.out.println(OldMsgQueue);
//         super.handleMsg(msg);
//    }

    // Guardie

    protected static void setGuardState(String state) {
        GuardPauseForward.setLastActiveState(state);
        GuardPauseBack.setLastActiveState(state);
    }

    protected static abstract class GuardPauseForward {
        protected static String lastActiveState;

        public static void setLastActiveState(String lastActiveState) {
            GuardPauseForward.setLastActiveState(lastActiveState);
        }

        public boolean eval( ) {
            return lastActiveState.equals(STATE_FORWARD)
                    || lastActiveState.equals(STATE_WALL_FORWARD)
                    || lastActiveState.equals(STATE_INIT);
        }
    }
    protected static abstract class GuardPauseBack {
        protected static String lastActiveState;

        public static void setLastActiveState(String lastActiveState) {
            GuardPauseBack.setLastActiveState(lastActiveState);
        }

        public boolean eval( ) {
            return lastActiveState.equals(STATE_BACK)
                    || lastActiveState.equals(STATE_WALL_BACK);
        }
    }
}
