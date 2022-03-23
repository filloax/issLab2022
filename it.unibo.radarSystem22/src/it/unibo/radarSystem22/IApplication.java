package it.unibo.radarSystem22;

public interface IApplication {
    void doJob(String domainConfigFile, String systemConfigFile);
    String getName();
}
