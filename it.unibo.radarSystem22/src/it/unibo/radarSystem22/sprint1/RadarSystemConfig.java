package it.unibo.radarSystem22.sprint1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import org.json.*;


public class RadarSystemConfig {
	public static int DLIMIT              =  30;
	public static boolean sonarObservable = true;
	public static boolean radarGuiRemote = false;
	public static int serverPort          = 8023;
	public static int ledPort            = 8040;
	public static int sonarPort          = 8041;
	public static int sonarObserverPort  = 8042;
	public static String hostAddr         = "localhost";
	public static String raspAddr         = "192.168.1.15";

	public static void setTheConfiguration(  ) {
		setTheConfiguration("../RadarSystemConfig.json");
	}

	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName � in una dir che include la bin
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);

			JSONTokener tokener = getTokener(resourceName);
			JSONObject object = new JSONObject(tokener);

			DLIMIT = object.getInt("DLIMIT");
			sonarObservable = object.getBoolean("sonarObservable");
			radarGuiRemote = object.getBoolean("radarGuiRemote");
			serverPort = object.getInt("serverPort");
			ledPort = object.getInt("ledPort");
			sonarPort = object.getInt("sonarPort");
			sonarObserverPort = object.getInt("sonarObserverPort");
			hostAddr = object.getString("hostAddr");
			raspAddr = object.getString("raspAddr");

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
			object.put("DLIMIT", DLIMIT);
			object.put("sonarObservable", sonarObservable);
			object.put("radarGuiRemote", radarGuiRemote);
			object.put("serverPort", serverPort);
			object.put("ledPort", ledPort);
			object.put("sonarPort", sonarPort);
			object.put("sonarObserverPort", sonarObserverPort);
			object.put("hostAddr", hostAddr);
			object.put("raspAddr", raspAddr);
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
