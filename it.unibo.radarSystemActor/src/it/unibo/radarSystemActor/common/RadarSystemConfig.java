package it.unibo.radarSystemActor.common;

import java.util.Optional;

import it.unibo.radarSystem22.domain.utils.StaticConfig;

import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.utils.ColorsOut;




public class RadarSystemConfig {
 	public static boolean tracing         = false;	
	public static boolean testing         = false;			
	public static int DLIMIT              =  50;     	
	public static boolean  RadarGuiRemote = false;
	
 
 	public static String hostAddr         = "localhost";		
	public static String raspAddr         = "localhost";		
 
	
	public static ProtocolType protocolType = ProtocolType.tcp;
	public static int  ctxServerPort      = 8018;
	
	//Aggiunte dopo Eventi 	
	public static boolean sonarObservable = false;
	
	public static void setTheConfiguration(  ) {
		setTheConfiguration("../RadarSystemConfig.json");
	}
	
	public static void setTheConfiguration( String resourceName ) {
		StaticConfig.setTheConfiguration(RadarSystemConfig.class, resourceName,
				(field, toSave) -> {
					if (field.getName().equals("protocolType")) {
						switch ((ProtocolType) toSave) {
							case tcp: return Optional.of("tcp");
							case coap : return Optional.of("coap");
							case mqtt : return Optional.of("mqtt");
						}
					}
					return Optional.empty();
				}, (field, toLoad) -> {
					if (field.getName().equals("protocolType")) {
						switch ((String) toLoad) {
							case "tcp"  : return Optional.of(ProtocolType.tcp);
							case "coap" : return Optional.of(ProtocolType.coap);
							case "mqtt" : return Optional.of(ProtocolType.mqtt);
						}
					}
					return Optional.empty();
				});
	}	
	 
}
