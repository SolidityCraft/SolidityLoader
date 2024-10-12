package io.soliditycraft.solidityloader.listener;

import io.soliditycraft.solidityloader.SolidityLoader;
import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public class SolidityListener implements Listener {

    private SolidityLoader loader;

    public synchronized void initialize(SolidityLoader loader) {
        this.loader = loader;
    }
}
