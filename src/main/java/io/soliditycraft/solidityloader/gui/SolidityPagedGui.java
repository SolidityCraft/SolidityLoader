package io.soliditycraft.solidityloader.gui;

import io.soliditycraft.solidityloader.listener.SolidityListener;
import io.soliditycraft.solidityloader.listener.SolidityListenerManager;
import io.soliditycraft.solidityloader.utils.SolUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PagedSolidityGui} class provides paginated inventory GUIs for players,
 * allowing navigation between pages of items. It supports adding custom items with actions
 * and provides "Next" and "Previous" page functionality.
 */
public class SolidityPagedGui extends SolidityListener {

    /**
     * A list of {@link SolidityItem} objects representing all items across all pages.
     */
    private final List<SolidityItem> items = new ArrayList<>();
    /**
     * Number of items displayed per page (excluding navigation buttons).
     */
    private final int itemsPerPage;
    /**
     * The Bukkit {@link Inventory} that represents the current page of the GUI.
     */
    private final Inventory gui;
    /**
     * The current page number the player is viewing (0-indexed).
     */
    private int currentPage = 0;

    /**
     * The maximum number of pages based on the items list.
     */
    private int maxPages;

    /**
     * Creates a new {@code PagedSolidityGui} instance.
     *
     * @param holder       the {@link InventoryHolder} for this inventory, can be null.
     * @param size         the size of the inventory (must be a multiple of 9, usually 54 for pagination).
     * @param title        the title of the inventory as seen by the player.
     * @param itemsPerPage number of items to show per page.
     */
    public SolidityPagedGui(InventoryHolder holder, int size, String title, int itemsPerPage) {
        this.gui = Bukkit.createInventory(holder, size, title);
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * Populates the GUI with items for the current page, adding navigation buttons
     * ("Next" and "Previous") where appropriate.
     */
    private void createGuiItems() {
        // Clear inventory first
        gui.clear();

        // Calculate the start and end index for the current page
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());

        // Populate the current page's items
        for (int i = startIndex; i < endIndex; i++) {
            SolidityItem item = items.get(i);
            gui.setItem(item.getPosition(), item.getItem());
        }

        // Add navigation buttons
        addNavigationButtons();
    }

    /**
     * Adds "Next" and "Previous" buttons to the inventory if there are multiple pages.
     */
    private void addNavigationButtons() {
        // Create "Next Page" button
        if (currentPage < maxPages - 1) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta meta = nextPage.getItemMeta();
            meta.setDisplayName("§aNext Page");
            nextPage.setItemMeta(meta);
            gui.setItem(gui.getSize() - 1, nextPage); // Last slot for "Next"
        }

        // Create "Previous Page" button
        if (currentPage > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta meta = prevPage.getItemMeta();
            meta.setDisplayName("§cPrevious Page");
            prevPage.setItemMeta(meta);
            gui.setItem(gui.getSize() - 9, prevPage); // Second to last row first slot for "Previous"
        }
    }

    /**
     * Opens the current page of the GUI for the specified player and registers the GUI as a listener.
     *
     * @param player the player who will see the inventory.
     * @return the current instance of {@code PagedSolidityGui}, useful for method chaining.
     */
    public SolidityPagedGui open(@NotNull Player player) {
        maxPages = (int) Math.ceil((double) items.size() / itemsPerPage);
        createGuiItems(); // Add items to the current page
        SolidityListenerManager.register(this); // Register this GUI as an event listener
        player.openInventory(gui); // Open the inventory for the player
        return this;
    }

    /**
     * Adds a {@code SolidityItem} to the list of items to be displayed in the GUI.
     *
     * @param item the item to be added.
     * @return the current instance of {@code PagedSolidityGui}, useful for method chaining.
     */
    public SolidityPagedGui addItem(SolidityItem item) {
        items.add(item);
        return this;
    }

    /**
     * Handles player interaction with the inventory by listening for {@link InventoryClickEvent}.
     * Handles clicks on "Next" and "Previous" page buttons, as well as item interactions.
     *
     * @param event the {@link InventoryClickEvent} triggered when a player interacts with the GUI.
     */
    @EventHandler
    public void handleInventoryClick(@NotNull InventoryClickEvent event) {
        if (!isInGui(event)) return; // Only handle clicks in this custom GUI

        event.setCancelled(true); // Prevent item movement

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        // If the clicked item is invalid, return
        if (isInvalidItem(clickedItem)) return;

        // Handle navigation buttons
        if (clickedItem.getType() == Material.ARROW) {
            String displayName = clickedItem.getItemMeta().getDisplayName();
            if (displayName.equals("§aNext Page")) {
                currentPage++;
                open(player); // Open the next page
            } else if (displayName.equals("§cPrevious Page")) {
                currentPage--;
                open(player); // Open the previous page
            }
            return;
        }

        // Find and execute the corresponding SolidityItem action
        SolidityItem item = SolUtils.find(items, (v) -> v.getItem().equals(clickedItem));
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
