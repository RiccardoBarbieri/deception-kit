package com.deceptionkit.dockerfile.options;

import com.deceptionkit.dockerfile.options.options.MountBindOptions;
import com.deceptionkit.dockerfile.options.types.RunMountOptionTypes;
import com.deceptionkit.dockerfile.options.types.RunNetworkOptionTypes;
import com.deceptionkit.dockerfile.options.types.RunSecurityOptionTypes;

public class RunCommandOptions extends CommandOptions {

    protected static final String MOUNT = "--mount";
    protected static final String NETWORK = "--network";
    protected static final String SECURITY = "--security";

    public RunCommandOptions mount(RunMountOptionTypes mount, MountBindOptions optionOptions) {
        this.options.add(RunCommandOptions.MOUNT + "=" + mount.getType() + optionOptions.build());
        return this;
    }

    public RunCommandOptions network(RunNetworkOptionTypes network) {
        this.options.add(RunCommandOptions.NETWORK + "=" + network.getType());
        return this;
    }

    public RunCommandOptions security(RunSecurityOptionTypes profile) {
        this.options.add(RunCommandOptions.SECURITY + "=" + profile.getType());
        return this;
    }
}
