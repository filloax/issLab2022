package unibo.actor22;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22.annotations.TransitionGuard;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public abstract class QakActor22FsmAnnot extends QakActor22Fsm {
    protected String initialState = null;

    public QakActor22FsmAnnot(String name) {
        super(name);
    }

    @Override
    protected void setTheInitialState() {
    }   //No more necessary for annotations

    @Override
    protected void declareTheStates() {
        try {
            Method[] methods = this.getClass().getDeclaredMethods();
			Method[] guards = Arrays.stream(methods)
					.filter(m -> m.isAnnotationPresent(TransitionGuard.class))
                    .toArray(Method[]::new);
            List<Method> annotatedMethods = new ArrayList<>(methods.length / 2);

            //ColorsOut.outappl("method: "+ m.length  , ColorsOut.CYAN);
            for (Method method : methods) {
                // imposta anche per ogni guardia
				method.setAccessible(true);
                if (method.isAnnotationPresent(State.class))
                    annotatedMethods.add(method);
            }
            for (Method method : annotatedMethods) {
				elabAnnotatedMethod(method, guards);
            }
        } catch (Exception e) {
            ColorsOut.outerr("readAnnots ERROR:" + e.getMessage());
        }
    }

    protected void setTheInitialState(String stateName) {
        if (initialState == null) {
            initialState = stateName;
            declareAsInitialState(initialState);
        } else ColorsOut.outerr("Multiple intial states not allowed");
    }

    protected Method getGuard(Method[] guards, String guardName) {
        return Arrays.stream(guards)
                .filter(m -> m.getName().equals(guardName))
                .findFirst()
                .orElse(null);
    }

    protected void elabStateMethod(Method m, String stateName, Method[] guards) {
        if (!m.getName().equals(stateName)) {
            ColorsOut.outerr(getName()
                    + " | QakActor22FsmAnnot  Method name must be the same as state name: was "
                    + m.getName() + ", expected " + stateName
            );
        }
        Vector<String> nextStates = new Vector<>();
        Vector<String> msgIds = new Vector<>();
        Vector<Method> transitionGuards = new Vector<>();

        Transition[] ta = m.getAnnotationsByType(Transition.class);

        for (Transition t : ta) {
            ColorsOut.outappl("Transition simple: " + t.msgId() + " -> " + t.state() + " guard=" + t.guard(), ColorsOut.CYAN);
            nextStates.add(t.state());
            msgIds.add(t.msgId());
            String guardName = t.guard();
            if (guardName.equals(""))
                transitionGuards.add(null);
            else {
                Method guard = getGuard(guards, guardName);
                if (guard == null) {
                    throw new IllegalArgumentException("Non existent guard name!");
                }
                transitionGuards.add(guard);
            }
        }
        //Farlo staticamente NO
// 		  ColorsOut.outappl("nextStates "+ nextStates.size() , ColorsOut.CYAN);
//		  ColorsOut.outappl("msgIds "+ msgIds.size() , ColorsOut.CYAN);
        doDeclareState(this, m, stateName, nextStates, msgIds, transitionGuards);

    }

    // Casting tramite polimorfismo necessario per poter
    // far funzionare invoke, che altrimenti lancia
    // java.lang.IllegalArgumentException: object is not an instance of declaring class
    protected void doDeclareState(
            QakActor22FsmAnnot castMyself, Method curMethod, String stateName,
            Vector<String> nextStates, Vector<String> msgIds,
            Vector<Method> guards
    ) {
        declareState(stateName, new StateActionFun() {
            @Override
            public void run(IApplMessage msg) {
                try {
                    //outInfo("uuuu "+ msg  + " " + this );
                    curMethod.invoke(castMyself, msg);  //I metodi hanno this come arg implicito
                    for (int j = 0; j < nextStates.size(); j++) {
                        Method g = guards.elementAt(j);
                        Boolean result = (g != null) ? (Boolean) g.invoke(castMyself) : true;
                        if (result) {
                            //ColorsOut.outappl("g:"+ g + " result=" + result.getClass().getName(), ColorsOut.GREEN);
                            addTransition(nextStates.elementAt(j), msgIds.elementAt(j));
                        }
                    }
                    nextState();
                } catch (Exception e) {
                    e.printStackTrace();
                    ColorsOut.outerr("wrong execution for:" + stateName + " - " + e.getMessage());
                }
            }
        });//declareState
    }

    protected void elabAnnotatedMethod(Method m, Method[] guards) {
        String functor = m.getName();
        Class<?>[] p = m.getParameterTypes();
//    		  ColorsOut.outappl("state ANNOT functor="+ functor + " p.length=" + p.length , ColorsOut.CYAN);
//    		  ColorsOut.outappl(""+p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") , ColorsOut.CYAN);
        if (p.length != 1
                || !p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage")
        ) {
            ColorsOut.outerr("wrong arguments for state:" + functor);
        } else {
            State stateAnnot = m.getAnnotation(State.class);
            if (stateAnnot.initial()) setTheInitialState(stateAnnot.name());
            //ColorsOut.outappl("state ANNOT name= "+ stateAnnot.name() + " initialState=" + initialState, ColorsOut.CYAN);
            elabStateMethod(m, stateAnnot.name(), guards);
        }
    }


}
