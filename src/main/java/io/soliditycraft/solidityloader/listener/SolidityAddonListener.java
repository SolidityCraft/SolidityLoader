package io.soliditycraft.solidityloader.listener;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.addons.SolidityAddon;
import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public class SolidityAddonListener implements Listener {

    private SolidityAddon addon;
    private SolidityLoader loader;

    public synchronized void initialize(SolidityAddon addon) {
        this.addon = addon;
        this.loader = getAddon().getLoader();
    }
}
