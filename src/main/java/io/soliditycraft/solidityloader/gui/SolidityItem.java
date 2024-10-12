package io.soliditycraft.solidityloader.gui;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Builder
@Getter
public class SolidityItem {

    private ItemStack item;
    private int position;
    private SolidityItemExecutor executor;
    public interface SolidityItemExecutor {

        void run(Player executor, InventoryClickEvent event);

    }

}
