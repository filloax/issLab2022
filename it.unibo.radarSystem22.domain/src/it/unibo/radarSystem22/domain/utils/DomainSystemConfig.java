package it.unibo.radarSystem22.domain.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class DomainSystemConfig {
	public static  boolean simulation    = true;
	public static boolean ledAvailable = true;
	public static boolean sonarAvailable = true;

 	public static  boolean ledGui        = false;
	public static  boolean webCam        = false;
 
	public static int sonarDelay          =  100;     
	public static int sonarDistanceMax    =  150;     
	public static boolean sonarObservable =  false;
	public static int DLIMIT              =  15;
	public static int testingDistance     =  DLIMIT - 2;
	public static int sonarMockDelta = -1;
	public static int sonarMockStartDist = 100;
	public static boolean sonarMockVerbose = false;
    
	public static boolean tracing         = false;	
	public static boolean testing         = false;			
	

	public static void setTheConfiguration(  ) {
		setTheConfiguration("./DomainSystemConfig.json");
	}

	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName è in una dir che include la bin  
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);

			JSONTokener tokener = getTokener(resourceName);
			JSONObject object = new JSONObject(tokener);

			simulation = object.getBoolean("simulation");
			ledAvailable = object.getBoolean("ledAvailable");
			sonarAvailable = object.getBoolean("sonarAvailable");

			webCam = object.getBoolean("webCam");

			sonarObservable = object.getBoolean("sonarObservable");
			sonarDelay = object.getInt("sonarDelay");
			sonarDistanceMax = object.getInt("sonarDistanceMax");
			DLIMIT = object.getInt("DLIMIT");
			tracing = object.getBoolean("tracing");
			testing = object.getBoolean("testing");
			sonarMockDelta = object.getInt("sonarMockDelta");
			sonarMockStartDist = object.getInt("sonarMockStartDist");
			sonarMockVerbose = object.getBoolean("sonarMockVerbose");

		} catch (FileNotFoundException | JSONException e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
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
