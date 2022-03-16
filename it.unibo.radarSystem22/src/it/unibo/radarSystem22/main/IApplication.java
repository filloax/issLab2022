package it.unibo.radarSystem22.main;

public interface IApplication {
    public void doJob(String domainConfigFile, String systemConfigFile);
    public String getName();
}
