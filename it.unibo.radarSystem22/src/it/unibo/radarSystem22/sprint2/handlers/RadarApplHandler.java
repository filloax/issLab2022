package it.unibo.radarSystem22.sprint2.handlers;
 
import org.json.JSONException;
import org.json.JSONObject;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.ColorsOut;


public class RadarApplHandler extends ApplMsgHandler {
	private IRadarDisplay radar;

	public RadarApplHandler(String name, IRadarDisplay radar) {
		super(name);
		this.radar = radar; 
	}
 	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message + " conn=" + conn);
		if(message.equals("getCurDistance")) {
			try {
				//conn.forward(""+curDistance);
				conn.reply(radar.getCurDistance().toString());
			} catch (Exception e) {
 				e.printStackTrace();
			}
			return;
		}
		//{ "distance" : 90 , "angle" : 90 }
		String distance = null, angle = null;
		try {
			JSONObject jsonObj = new JSONObject(message);
			distance = Integer.toString(jsonObj.getInt("distance"));
			angle = Integer.toString(jsonObj.getInt("angle"));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		radar.update(distance, angle);
	}


 

}
