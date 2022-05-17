package unibo.Robots.cleaner;

import it.unibo.kactor.IApplMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import unibo.Robots.common.VRobotMoves;
import unibo.Robots.common.WsConnApplObserver;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22.annotations.TransitionGuard;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;

import java.util.Arrays;

public class RobotCleaner extends QakActor22FsmAnnot {
    private Interaction2021 conn;
    private float timePerUnit; // movimento per fare 1 unità, vedi VRobotMoves
    private int turnTime;
    float positionX, positionY; // relativo a home; Y è direzione colonne
    private String lastActiveState;
    private Direction currentDirection;

    public static final String STATE_INIT = "init";
    public static final String STATE_WAIT_START = "waitStart";
    public static final String STATE_FORWARD = "moveForward";
    public static final String STATE_WALL_FORWARD = "collidedForward";
    public static final String STATE_BACK = "moveBack";
    public static final String STATE_WALL_BACK = "collidedBack";
    public static final String STATE_LAST_COLUMN = "lastColumn";
    public static final String STATE_GO_HOME_START = "goHomeStart";
    public static final String STATE_GO_HOME = "goHome";
    public static final String STATE_GO_HOME_END = "goHomeEnd";
    //public static final String STATE_END = "endState";
    public static final String STATE_PAUSE = "pause";

    public RobotCleaner(String name) {
        super(name);
        timePerUnit = 300;
        turnTime = 300;
        positionX = 0;
        positionY = 0;
        lastActiveState = "";
        currentDirection = Direction.DOWN;
    }

    @State(name = STATE_INIT, initial = true)
    @Transition(state = STATE_WAIT_START)
    protected void init(IApplMessage msg) {
        outInfo(msg.toString());

        outInfo(msg + " connecting (blocking all the actors ) ... ");
        conn = WsConnection.create("localhost:8091");
        outInfo("connected " + conn);
        ((WsConnection) conn).addObserver(new WsConnApplObserver(getName(), false));
    }

    @State(name = STATE_WAIT_START)
    @Transition(state = STATE_FORWARD, msgId = SystemData.startSysCmdId)
    protected void waitStart(IApplMessage msg) {
        outInfo(msg.toString());
    }

    @State(name = STATE_FORWARD)
    @Transition(state = STATE_WALL_FORWARD, msgId = SystemData.endMoveKoId)
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_FORWARD, msgId = SystemData.endMoveOkId)
    protected void moveForward(IApplMessage msg) {
        outInfo(msg.toString());

        setLastActiveState(STATE_FORWARD);

        printPosition();
        step();
        // aggiungi intero step, al limite ridurremo poi la posizione
        addPosition(0, 1);
    }

    @State(name = STATE_WALL_FORWARD)
    @Transition(state = STATE_LAST_COLUMN, msgId = SystemData.endMoveKoId)
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_BACK, msgId = SystemData.endMoveOkId)
    protected void collidedForward(IApplMessage msg) {
        outInfo(msg.toString());

        setLastActiveState(STATE_WALL_FORWARD);

        JSONObject data = getMsgData(msg);
        // Aggiusta posizione in base a strada effettivamente percorsa
        addPosition(0, -1 + data.getInt("duration") / timePerUnit);

        printPosition();
        uTurnLeft();
        addPosition(1, 0);
    }

    @State(name = STATE_BACK)
    @Transition(state = STATE_WALL_BACK, msgId = SystemData.endMoveKoId)
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_BACK, msgId = SystemData.endMoveOkId)
    protected void moveBack(IApplMessage msg) {
        outInfo(msg.toString());

        setLastActiveState(STATE_BACK);

        printPosition();
        step();
        addPosition(0, -1);
    }

    @State(name = STATE_WALL_BACK)
    @Transition(state = STATE_GO_HOME_START, msgId = SystemData.endMoveKoId, guard = "guardEndGoHome")
    @Transition(state = STATE_LAST_COLUMN, msgId = SystemData.endMoveKoId, guard = "guardEndGoUp")
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_FORWARD, msgId = SystemData.endMoveOkId)
    protected void collidedBack(IApplMessage msg) {
        outInfo(msg.toString());

        setLastActiveState(STATE_WALL_BACK);

        JSONObject data = getMsgData(msg);
        // Aggiusta posizione in base a strada effettivamente percorsa
        addPosition(0, 1 - data.getInt("duration") / timePerUnit);

        printPosition();
        uTurnRight();
        addPosition(1, 0);
    }

    @State(name = STATE_LAST_COLUMN)
    @Transition(state = STATE_GO_HOME_START, msgId = SystemData.endMoveKoId)
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_LAST_COLUMN, msgId = SystemData.endMoveOkId)
    protected void lastColumn(IApplMessage msg) {
        outInfo(msg.toString());
        // Aggiusta posizione in base a strada effettivamente percorsa
        // da fare solo la prima volta, per ora commento non servendo la posizione
//        JSONObject data = getMsgData(msg);
//        addPosition(-1 + data.getInt("duration") / timePerUnit, 0);
//        printPosition();

        setLastActiveState(STATE_LAST_COLUMN);

        //printPosition();
        step();
        //addPosition(0, -1); // servirebbero due stati per distinguere, o tracciare direzione
    }

    @State(name = STATE_GO_HOME_START)
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_GO_HOME)
    protected void goHomeStart(IApplMessage msg) {
        outInfo(msg.toString());
        setLastActiveState(STATE_GO_HOME_START);

        if (currentDirection == Direction.UP) {
            turnLeft();
        } else if (currentDirection == Direction.DOWN) {
            turnRight();
        } else {
            System.err.println("Illegal direction when starting to go home: " + currentDirection);
        }
    }

    @State(name = STATE_GO_HOME)
    @Transition(state = STATE_GO_HOME_END, msgId = SystemData.endMoveKoId)
    @Transition(state = STATE_PAUSE, msgId = SystemData.stopSysCmdId)
    @Transition(state = STATE_GO_HOME, msgId = SystemData.endMoveOkId)
    protected void goHome(IApplMessage msg) {
        outInfo(msg.toString());
        setLastActiveState(STATE_GO_HOME);

        step();
    }

    @State(name = STATE_GO_HOME_END)
    @Transition(state = STATE_WAIT_START)
    protected void goHomeEnd(IApplMessage msg) {
        outInfo(msg.toString());

        turnLeft();
        System.out.println(getName() + " | finished!");
    }

//    @State(name = STATE_END)
//    @Transition(state = STATE_WAIT_START)
//    protected void endState(IApplMessage msg) {
//        outInfo(msg.toString());
//        System.out.println(getName() + " | finished!");
//    }

    @State(name = STATE_PAUSE)
    @Transition(state = STATE_FORWARD, msgId = SystemData.resumeSysCmdId, guard = "guardPauseForward")
    @Transition(state = STATE_BACK, msgId = SystemData.resumeSysCmdId, guard = "guardPauseBack")
    @Transition(state = STATE_LAST_COLUMN, msgId = SystemData.resumeSysCmdId, guard = "guardPauseLastColumn")
    @Transition(state = STATE_GO_HOME, msgId = SystemData.resumeSysCmdId, guard = "guardPauseGoHome")
    protected void pause(IApplMessage msg) {
        outInfo(msg.toString());
    }

    // Guards

    @TransitionGuard
    // If the robot should immediately turn (absolute) left and go home at the end
    protected boolean guardEndGoHome() {
        return currentDirection.equals(Direction.UP);
    }

    @TransitionGuard
    // If the robot should go back up before going home
    protected boolean guardEndGoUp() {
        return !guardEndGoHome();
    }

    @TransitionGuard
    protected boolean guardPauseForward() {
        return lastActiveState.equals(STATE_FORWARD)
                || lastActiveState.equals(STATE_WALL_FORWARD)
                || lastActiveState.equals(STATE_INIT);
    }

    @TransitionGuard
    protected boolean guardPauseBack() {
        return lastActiveState.equals(STATE_BACK)
                || lastActiveState.equals(STATE_WALL_BACK);
    }

    @TransitionGuard
    protected boolean guardPauseLastColumn() {
        return lastActiveState.equals(STATE_LAST_COLUMN);
    }

    @TransitionGuard
    protected boolean guardPauseGoHome() {
        return lastActiveState.equals(STATE_GO_HOME)
                || lastActiveState.equals(STATE_GO_HOME_START);
    }

    // Utils
    protected void step() {
        VRobotMoves.step(getName(), conn, true);
    }

    protected void uTurnLeft() {
        int curDirVal = currentDirection.get();
        int flippedDir = (curDirVal + 2) % 4;
        currentDirection = Direction.dir(flippedDir);
        VRobotMoves.turnLeftAndStep(getName(), turnTime, conn);
    }
    protected void uTurnRight() {
        int curDirVal = currentDirection.get();
        int flippedDir = (curDirVal + 2) % 4;
        currentDirection = Direction.dir(flippedDir);
        VRobotMoves.turnRightAndStep(getName(), turnTime, conn);
    }
    protected void turnLeft() {
        int curDirVal = currentDirection.get();
        System.out.println(curDirVal + " " + (curDirVal - 1) + " " + ((curDirVal - 1) % 4));
        currentDirection = Direction.dir(Math.floorMod(curDirVal - 1, 4));
        VRobotMoves.turnLeft(getName(), conn, true);
    }
    protected void turnRight() {
        int curDirVal = currentDirection.get();
        currentDirection = Direction.dir((curDirVal + 1) % 4);
        VRobotMoves.turnRight(getName(), conn, true);
    }

    protected void setLastActiveState(String state) {
        lastActiveState = state;
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

    @Override
    protected void outInfo(String msg) {
        super.outInfo(msg + " at " + (System.currentTimeMillis()));
    }

    public float getTimePerUnit() {
        return timePerUnit;
    }

    public void setTimePerUnit(float timePerUnit) {
        this.timePerUnit = timePerUnit;
    }

    public float getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(int turnTime) {
        this.turnTime = turnTime;
    }


    private static enum Direction {
        LEFT(0),
        UP(1),
        RIGHT(2),
        DOWN(3),
        ;

        private int val;
        private Direction(int val) {
            this.val = val;
        }

        public int get() { return val; }

        public static Direction dir(int val) {
            return Arrays.stream(Direction.values())
                    .filter(d -> d.val == val)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid direction value " + val));
        }
    }
}
