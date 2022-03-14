package it.unibo.radarSystem22.domain.concrete;

import it.unibo.radarSystem22.domain.models.LedModel;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

import java.io.IOException;

public class LedConcrete extends LedModel {
    private Runtime rt = Runtime.getRuntime();

    private static final String ON_SCRIPT_PATH  = "./resources/bash/led25GpioTurnOn.sh";
    private static final String OFF_SCRIPT_PATH = "./resources/bash/led25GpioTurnOff.sh";

    @Override
    protected void ledActivate(boolean val) {
        try {
            if (val)
                rt.exec("sudo " + ON_SCRIPT_PATH);
            else
                rt.exec("sudo " + OFF_SCRIPT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            ColorsOut.outerr("Could not set led state!");
        }
    }
}
