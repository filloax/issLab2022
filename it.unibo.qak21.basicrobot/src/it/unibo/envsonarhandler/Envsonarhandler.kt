/* Generated by AN DISI Unibo */ 
package it.unibo.envsonarhandler

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Envsonarhandler ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("envsonarhandler | START")
					}
					 transition(edgeName="t17",targetState="handleEnvSonar",cond=whenEvent("sonar"))
				}	 
				state("handleEnvSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t18",targetState="handleEnvSonar",cond=whenEvent("sonar"))
				}	 
			}
		}
}
