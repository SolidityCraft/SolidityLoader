package io.soliditycraft.solidityloader.gui;

import io.soliditycraft.solidityloader.listener.SolidityListener;
import io.soliditycraft.solidityloader.listener.SolidityListenerManager;
import io.soliditycraft.solidityloader.utils.SolUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code SolidityGui} class is responsible for creating and managing custom inventory GUIs.
 * It provides functionality to open a custom inventory for players, handle inventory interactions,
 * and store custom items (represented by {@code SolidityItem}) within the GUI.
 * <p>
 * This class extends {@code SolidityListener}, allowing it to listen for inventory click events and
 * perform actions based on user interaction with the GUI.
 */
@Getter
public class SolidityGui extends SolidityListener {

    /**
     * The Bukkit {@link Inventory} that represents the custom GUI.
     */
    private final Inventory gui;

    /**
     * A list of {@link SolidityItem} objects representing the items in the GUI.
     */
    private final List<SolidityItem> items = new ArrayList<>();
    private final int size;
    private final InventoryHolder holder;
    private final String title;

    /**
     * Creates a new {@code SolidityGui} instance.
     *
     * @param holder the {@link InventoryHolder} for this inventory, can be null.
     * @param size   the size of the inventory (must be a multiple of 9).
     * @param title  the title of the inventory as seen by the player.
     */
    public SolidityGui(InventoryHolder holder, int size, String title) {
        this.holder = holder;
        this.size = size;
        this.title = title;
        this.gui = Bukkit.createInventory(holder, size, title);
    }

    /**
     * Populates the GUI with items from the {@code items} list by setting each item
     * in the appropriate position within the inventory.
     */
    private void createGuiItems() {
        for (SolidityItem item : items) {
            this.gui.setItem(item.getPosition(), item.getItem());
        }
    }

    /**
     * Opens the GUI for a specified player and registers this GUI as a listener for inventory events.
     *
     * @param player the player who will see the inventory.
     * @return the current instance of {@code SolidityGui}, useful for method chaining.
     */
    public SolidityGui open(@NotNull Player player) {
        createGuiItems(); // Add items to the GUI before opening
        SolidityListenerManager.register(this); // Register this GUI as an event listener
        player.openInventory(gui); // Open the inventory for the player
        return this;
    }

    /**
     * Adds a {@code SolidityItem} to the list of items to be displayed in the GUI.
     *
     * @param item the item to be added.
     * @return the current instance of {@code SolidityGui}, useful for method chaining.
     */
    public SolidityGui addItem(SolidityItem item) {
        items.add(item);
        return this;
    }

    /**
     * Handles player interaction with the inventory by listening for {@link InventoryClickEvent}.
     * If a player clicks an item in the custom GUI, the associated {@code SolidityItem} executor
     * will be run.
     *
     * @param event the {@link InventoryClickEvent} triggered when a player interacts with the GUI.
     */
    @EventHandler
    public void handleInventoryClick(@NotNull InventoryClickEvent event) {
        // Only handle clicks in this custom GUI
        if (!isInGui(event)) return;

        event.setCancelled(true); // Prevent item movement

        Player player = (Player) event.getWhoClicked(); // Get the player interacting
        ItemStack clickedItem = event.getCurrentItem();

        // If the clicked item is invalid, return
        if (isInvalidItem(clickedItem)) return;

        // Find the corresponding SolidityItem in the GUI based on the clicked item
        SolidityItem item = SolUtils.find(items, (v) -> v.getItem().equals(clickedItem));

        // Execute the item's action if found
        if (item != null) {
            item.getExecutor().run(player, event);
        }
    }

    /**
     * Checks whether the click event occurred inside this GUI.
     *
     * @param event the {@link InventoryClickEvent} being checked.
     * @return {@code true} if the event happened in this GUI; {@code false} otherwise.
     */
    private boolean isInGui(@NotNull InventoryClickEvent event) {
        return event.getClickedInventory() == gui;
    }

    /**
     * Checks if the clicked item is invalid (null or air).
     *
     * @param item the {@link ItemStack} to be checked.
     * @return {@code true} if the item is invalid; {@code false} otherwise.
     */
    private boolean isInvalidItem(ItemStack item) {
        return item == null || item.getType().isAir();
    }
}
