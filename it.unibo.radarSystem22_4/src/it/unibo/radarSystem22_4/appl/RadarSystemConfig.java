package it.unibo.radarSystem22_4.appl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import it.unibo.radarSystem22.domain.utils.StaticConfig;
import org.json.JSONObject;
import org.json.JSONTokener;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.ProtocolType;


public class RadarSystemConfig {
 	public static boolean tracing         = false;	
	public static boolean testing         = false;			
	public static int DLIMIT              =  15;     	
	public static boolean  RadarGuiRemote = false;
	
 
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
