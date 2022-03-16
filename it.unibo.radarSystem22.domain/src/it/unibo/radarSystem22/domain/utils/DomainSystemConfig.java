package it.unibo.radarSystem22.domain.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;


public class DomainSystemConfig {
	public static  boolean simulation    = true;
	public static boolean ledAvailable = true;
	public static boolean sonarAvailable = true;
	public static boolean radarAvailable = false;

 	public static  boolean ledGui        = false;
	public static  boolean radarRemote   = false;

	public static int sonarDelay          =  100;     
	public static int sonarDistanceMax    =  150;     
	public static boolean sonarObservable =  false;
	public static int sonarMockDelta = -1;
	public static int sonarMockStartDist = 100;
	public static boolean sonarVerbose = false;
    
	public static boolean tracing         = false;	
	public static boolean testing         = false;			
	

	public static void setTheConfiguration(  ) {
		setTheConfiguration("../DomainSystemConfig.json");
	}

	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName � in una dir che include la bin  
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);

			JSONTokener tokener = getTokener(resourceName);
			JSONObject object = new JSONObject(tokener);

			simulation = object.getBoolean("simulation");
			ledAvailable = object.getBoolean("ledAvailable");
			sonarAvailable = object.getBoolean("sonarAvailable");
			radarAvailable = object.getBoolean("radarAvailable");

			radarRemote = object.getBoolean("radarRemote");

			sonarObservable = object.getBoolean("sonarObservable");
			sonarDelay = object.getInt("sonarDelay");
			sonarDistanceMax = object.getInt("sonarDistanceMax");
			tracing = object.getBoolean("tracing");
			testing = object.getBoolean("testing");
			sonarMockDelta = object.getInt("sonarMockDelta");
			sonarMockStartDist = object.getInt("sonarMockStartDist");
			sonarVerbose = object.getBoolean("sonarVerbose");

		} catch (JSONException e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
		} catch (FileNotFoundException e) {
			ColorsOut.outappl("Config file not found, saving default config file to " + resourceName, ColorsOut.YELLOW);
			saveConfigFile(resourceName);
		}

	}

	private static void saveConfigFile(String resourceName) {
		FileWriter fw = null;
		JSONObject object = new JSONObject();
		try {
			object.put("simulation", simulation);
			object.put("ledAvailable", ledAvailable);
			object.put("sonarAvailable", sonarAvailable);
			object.put("radarAvailable", radarAvailable);
			
			object.put("radarRemote", radarRemote);
			
			object.put("sonarObservable", sonarObservable);
			object.put("sonarDelay", sonarDelay);
			object.put("sonarDistanceMax", sonarDistanceMax);
			object.put("tracing", tracing);
			object.put("testing", testing);
			object.put("sonarMockDelta", sonarMockDelta);
			object.put("sonarMockStartDist", sonarMockStartDist);
			object.put("sonarVerbose", sonarVerbose);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			fw = new FileWriter(resourceName);
			fw.write(object.toString(4));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static JSONTokener getTokener(String resourceName) throws FileNotFoundException {
			FileReader reader = new FileReader(resourceName);
			JSONTokener tokener;
			/* Sembra usare Reader, non InputStream
			FileInputStream fis = new FileInputStream(new File(resourceName));
	        JSONTokener tokener = new JSONTokener(fis);
			 */

			tokener = new JSONTokener(reader);
			return tokener;
	}
	 
}
