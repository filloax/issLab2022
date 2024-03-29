package it.unibo.radarSystem22.sprint2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.sprint1.RadarSystemSprint1Main;
import it.unibo.radarSystem22.sprint2.main.sysOnRasp.RadarSysSprint2ControllerOnRaspMain;
import it.unibo.radarSystem22.sprint2a.main.devicesOnRasp.RadarSysSprint2aDevicesOnRaspMain;
import it.unibo.radarSystem22.sprint3.main.devicesOnRasp.RadarSysSprint3DevicesOnRaspMain;

public class MainSelectOnRasp {
	public HashMap<String,IApplication> programs = new HashMap<String,IApplication>();
	
	protected void outMenu() {
		for (String i : programs.keySet()) { //
			  System.out.println( i + "\t" + programs.get(i).getName() );
		}
 	}
	public void doChoice() {
		programs.put("1", new RadarSystemSprint1Main() );
		programs.put("2", new RadarSysSprint2ControllerOnRaspMain());
		programs.put("3", new RadarSysSprint2aDevicesOnRaspMain());
		programs.put("4", new RadarSysSprint3DevicesOnRaspMain());
		String i = "";
		outMenu();
		ColorsOut.outappl(">>>   ", ColorsOut.ANSI_PURPLE);
		BufferedReader inputr = new BufferedReader(new InputStreamReader(System.in));
		try {
			i = inputr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		programs.get(i).doJob("DomainSystemConfig.json","RadarSystemConfig.json");
}
	public static void main( String[] args) throws Exception {
		ColorsOut.outappl("---------------------------------------------------", ColorsOut.BLUE);
		ColorsOut.outappl("MainSelectOnRasp: this application uses Config Files", ColorsOut.BLUE);
		ColorsOut.outappl("---------------------------------------------------", ColorsOut.BLUE);
		new MainSelectOnRasp().doChoice();
	}
}
