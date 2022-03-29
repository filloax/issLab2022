package it.unibo.radarSystem22.sprint1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.StaticConfig;
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

	public static void setTheConfiguration() {
		setTheConfiguration("../RadarSystemConfig.json");
	}

	public static void setTheConfiguration(String resourceName) {
		StaticConfig.setTheConfiguration(RadarSystemConfig.class, resourceName);
	}

}
