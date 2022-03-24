package it.unibo.radarSystem22.sprint3.interpreters;

import it.unibo.radarSystem22.domain.interfaces.ISonar;

public class SonarApplInterpreter implements IApplInterpreter {
    private ISonar sonar;

    public SonarApplInterpreter(ISonar sonar) {
        this.sonar = sonar;
    }

    @Override
    public String elaborate(String message) {
        switch (message) {
            case "activate":
                sonar.activate();
                break;
            case "deactivate":
                sonar.deactivate();
                break;
            case "getDistance":
                return sonar.getDistance().toString();
            case "isActive":
                return Boolean.toString(sonar.isActive());
            default:
                throw new UknownCommandException("Unknown Sonar command: " + message);
        }
        return "done: " + message;
    }
}
