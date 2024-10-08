package io.soliditycraft.solidityloader.addons;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.commands.SolidityCommandManager;
import lombok.Getter;

@Getter
public abstract class SolidityAddon {

    private SolidityLoader loader;
    private SolidityAddonData info;

    synchronized void initialize(SolidityLoader loader, SolidityAddonData info) {
        this.setLoader(loader);
        this.info = info;
    }

    /**
     * Event emitted on load when the addon loads
     */
    public abstract void onLoad();

    /**
     * Event emitted on enable when the addon enables
     */
    public abstract void onEnable();

    /**
     * Event emitted on disable when the addon disable
     */
    public abstract void onDisable();

    public SolidityCommandManager getCommandManager() {
        return getLoader().getCommandManager();
    }

    private void setLoader(SolidityLoader loader) {
        this.loader = loader;
    }
}
