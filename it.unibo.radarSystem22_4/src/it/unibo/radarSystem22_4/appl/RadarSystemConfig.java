package it.unibo.radarSystem22_4.appl;

import it.unibo.radarSystem22.domain.utils.StaticConfig;

import it.unibo.radarSystem22_4.comm.ProtocolType;


public class RadarSystemConfig {
 	public static boolean tracing            = false;
	public static boolean testing            = false;
	public static int DLIMIT                 =  15;
	public static boolean radarGuiRemote = false;
	public static boolean useSonarObservable = true;
 
 	public static String hostAddr         = "localhost";		
	public static String raspAddr         = "localhost";		
 
	//Aggiunte dello SPRINT4 	
	public static ProtocolType protcolType= ProtocolType.tcp;		
	public static int  ctxServerPort      = 8018;
	
	public static void setTheConfiguration(  ) {
		setTheConfiguration("../RadarSystemConfig.json");
	}
	
	public static void setTheConfiguration(String resourceName) {
		StaticConfig.setTheConfiguration(RadarSystemConfig.class, resourceName);
	}	
	 
}
