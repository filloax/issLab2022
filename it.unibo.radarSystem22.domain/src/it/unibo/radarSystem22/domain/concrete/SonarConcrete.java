package it.unibo.radarSystem22.domain.concrete;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SonarConcrete extends SonarModel {
    private Process proc;
    private BufferedReader procOutReader;

    private static final String BIN_PATH = "./resources/c/SonarAlone";

    @Override
    protected void sonarSetUp() {
        curVal = new Distance(DomainSystemConfig.sonarDistanceMax);
    }

    @Override
    public void activate() {
        if (proc == null) {
            try {
                proc = Runtime.getRuntime().exec("sudo " + BIN_PATH);
                procOutReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                ColorsOut.outerr("Could not start sonar control process, aborting...");
                return;
            }
        }
        super.activate();
    }

    @Override
    protected void sonarProduce() {
        String distanceLine = null;
        try {
            distanceLine = procOutReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (distanceLine == null)
            return;
        int val = Integer.parseInt(distanceLine);
        int lastVal = curVal.getVal();
        if (lastVal != val && val < DomainSystemConfig.sonarDistanceMax) {
            updateDistance(val);
        }
    }

    @Override
    public void deactivate() {
        curVal = new Distance(DomainSystemConfig.sonarDistanceMax);
        if (proc != null) {
            proc.destroy();
            proc = null;
        }
        super.deactivate();
    }

}
