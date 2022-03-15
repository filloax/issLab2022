package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import org.junit.Before;
import org.junit.Test;

public class TestSonarReal {

    @Before
    public void up() {
        DomainSystemConfig.setTheConfiguration();
    }

    @Test
    public void testSonar() {
        int maxDelta = 1;

        if (!DomainSystemConfig.sonarAvailable) {
            ColorsOut.outappl("Sonar device not available, won't do real sonar test", ColorsOut.ANSI_YELLOW);
            return;
        }

        DomainSystemConfig.simulation = false;

        ColorsOut.outappl("Running REAL sonar test", ColorsOut.ANSI_PURPLE);
        ColorsOut.outappl("Assicurarsi che il sonar sia fissato e " +
                "posizionato davanti ad un oggetto fermo", ColorsOut.ANSI_YELLOW);

        ISonar sonar = DeviceFactory.createSonar();
        SonarTestConsumer testConsumer = new SonarTestConsumer(sonar, maxDelta);
        sonar.activate();
        testConsumer.start();
        // Se il risultato è coerente per 3 secondi successo, altrimenti termina
        // con errore dentro a SonarTestConsumer
        BasicUtils.delay(2000);
        int lastVal = sonar.getDistance().getVal();
        sonar.deactivate();
        ColorsOut.outappl("Sonar ha fornito lo stesso output per 2 secondi con successo", ColorsOut.GREEN);
        System.out.println("Valore finale: " + lastVal);
    }
}
