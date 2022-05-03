package unibo.actor22;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Vector;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22.annotations.TransitionGuard;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
 
 

public abstract class QakActor22FsmAnnot  extends QakActor22Fsm{
	protected QakActor22FsmAnnot myself;
	protected String initialState = null;

	public QakActor22FsmAnnot(String name) {
		super(name);
		myself = this;
 	}

	@Override
	protected void setTheInitialState(  ) {	}   //No more necessary for annotations
	
	@Override
	protected void declareTheStates( ) {
    	try {
 		  Method[] m = this.getClass().getDeclaredMethods( );
		  //ColorsOut.outappl("method: "+ m.length  , ColorsOut.CYAN);		      
  		  for( int i=0; i<m.length; i++ ) {
  			  m[i].setAccessible(true);
 		      if( m[i].isAnnotationPresent(State.class)) elabAnnotatedMethod(m[i]);	  
  		  }
   		} catch (Exception e) {
  			ColorsOut.outerr("readAnnots ERROR:" + e.getMessage() )   ;	
  		}		
	}
	
	protected void setTheInitialState( String stateName ) {
		 if( initialState == null  ) {
			initialState= stateName;
			declareAsInitialState( initialState );
		 } else ColorsOut.outerr("Multiple intial states not allowed" );
	}	

	protected Method getGuard(String guardName) {
		Method[] methods = this.getClass().getDeclaredMethods();
		return Arrays.stream(methods)
				.filter(m -> m.isAnnotationPresent(TransitionGuard.class))
				.findFirst()
				.orElse(null);
	}

	protected void elabStateMethod(Method m, String stateName) {
		if( ! m.getName().equals(stateName)) {
			ColorsOut.outerr(getName() + " | QakActor22FsmAnnot  Method name must be the same as state name" );
		}
		  Vector<String> nextStates = new Vector<>();
		  Vector<String> msgIds     = new Vector<>();
		  Vector<Method> guards      = new Vector<>();
		  
		  Transition[] ta = m.getAnnotationsByType(Transition.class);
 		  
		  for ( Transition t : ta ) {
			  ColorsOut.outappl("Transition simple: "+ t.msgId() + " -> " + t.state() + " guard=" + t.guard(), ColorsOut.CYAN);
			  nextStates.add(t.state());
			  msgIds.add(t.msgId());
			  String guardName = t.guard();
			  if (guardName.equals(""))
				  guards.add(null);
			  else {
				  Method guard = getGuard(guardName);
				  if (guard == null) {
					  throw new IllegalArgumentException("Non existent guard name!");
				  }
				  guards.add(guard);
			  }
 		  }
		  //Farlo staticamente NO
// 		  ColorsOut.outappl("nextStates "+ nextStates.size() , ColorsOut.CYAN);
//		  ColorsOut.outappl("msgIds "+ msgIds.size() , ColorsOut.CYAN);
		  doDeclareState(m,stateName,nextStates,msgIds,guards );	
		   
	}
	
	protected boolean guardForTransition(String stateName, String transName ) {
		return false;
	}
	
	protected void doDeclareState(
			Method curMethod, String stateName,
			Vector<String> nextStates, Vector<String> msgIds, Vector<Method> guards) {
	  	declareState( stateName, new StateActionFun() {
			@Override
			public void run( IApplMessage msg ) {
				try {
					//outInfo("uuuu "+ msg  + " " + this );
					curMethod.invoke(  myself, msg   );  //I metodi hanno this come arg implicito
					for( int j=0; j<nextStates.size();j++ ) {
						Method g   =  guards.elementAt(j);
						Boolean result = (Boolean) g.invoke(this);
						if( result ) {
							//ColorsOut.outappl("g:"+ g + " result=" + result.getClass().getName(), ColorsOut.GREEN);
							addTransition( nextStates.elementAt(j), msgIds.elementAt(j) );
						}
					}
					nextState();
				} catch ( Exception e) {
					e.printStackTrace();
					ColorsOut.outerr("wrong execution for:"+ stateName + " - " + e.getMessage());
				}
			}
	  	});//declareState
	}
	
	protected void elabAnnotatedMethod(Method m) {
		String functor =  m.getName();		    	 
		Class<?>[] p   =  m.getParameterTypes();
//    		  ColorsOut.outappl("state ANNOT functor="+ functor + " p.length=" + p.length , ColorsOut.CYAN);
//    		  ColorsOut.outappl(""+p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") , ColorsOut.CYAN);
		if(p.length != 1 ||
				!p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage")) {
    			  ColorsOut.outerr("wrong arguments for state:"+ functor);
		}else {
			State stateAnnot=m.getAnnotation(State.class);
			if( stateAnnot.initial() )  setTheInitialState(stateAnnot.name());
			//ColorsOut.outappl("state ANNOT name= "+ stateAnnot.name() + " initialState=" + initialState, ColorsOut.CYAN);
			elabStateMethod( m, stateAnnot.name());		  
		}
 	}
	

}
