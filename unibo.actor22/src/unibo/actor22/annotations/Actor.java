package unibo.actor22.annotations;

import unibo.actor22comm.ProtocolType;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


//@Target( value = {ElementType.CONSTRUCTOR,ElementType.METHOD, ElementType.TYPE} )
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Actors.class)
public @interface Actor {
	String name();
	boolean local() default true;
	@SuppressWarnings("rawtypes")
	Class implement() default void.class;
	String host() default "";
	String port() default "";
	ProtocolType protocol() default ProtocolType.tcp;
}
