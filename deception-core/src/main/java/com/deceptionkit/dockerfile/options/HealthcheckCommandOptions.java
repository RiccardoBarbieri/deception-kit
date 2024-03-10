package com.deceptionkit.dockerfile.options;

public class HealthcheckCommandOptions extends CommandOptions {

    protected static final String INTERVAL = "--interval";
    protected static final String TIMEOUT = "--timeout";
    protected static final String START_PERIOD = "--start-period";
    protected static final String START_INTERVAL = "--start-interval";
    protected static final String RETRIES = "--retries";

    public HealthcheckCommandOptions interval(String interval) {
        this.options.add(HealthcheckCommandOptions.INTERVAL + "=" + interval);
        return this;
    }

    public HealthcheckCommandOptions timeout(String timeout) {
        this.options.add(HealthcheckCommandOptions.TIMEOUT + "=" + timeout);
        return this;
    }

    public HealthcheckCommandOptions startPeriod(String startPeriod) {
        this.options.add(HealthcheckCommandOptions.START_PERIOD + "=" + startPeriod);
        return this;
    }

    public HealthcheckCommandOptions startInterval(String startInterval) {
        this.options.add(HealthcheckCommandOptions.START_INTERVAL + "=" + startInterval);
        return this;
    }

    public HealthcheckCommandOptions retries(Integer retries) {
        this.options.add(HealthcheckCommandOptions.RETRIES + "=" + retries.toString());
        return this;
    }

}
