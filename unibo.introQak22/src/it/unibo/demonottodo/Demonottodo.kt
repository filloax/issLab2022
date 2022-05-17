/* Generated by AN DISI Unibo */ 
package it.unibo.demonottodo

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Demonottodo ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						
								fun fibo(n: Int) : Int {
									if( n < 0 ) throw Exception("fibo argument must be >0")
									if( n == 0 || n==1 ) return n
									return fibo(n-1)+fibo(n-2)
								}
								println("--------------------------------------------------------------------------------")
								println("This actor performs an activity completely expressed in Kotlin")	
								val n = 7
								val v = fibo(n)	
								println("fibo($n)=$v")
								println("Since this should be a MODEL, this example is meaningless")
								println("It is far better to define this code in a Kotlin (or Java) class C and write ")
								println("run 'C'")			
								println("--------------------------------------------------------------------------------")
								fibo(-3) //just to look at whar happens ...
					}
				}	 
			}
		}
}
