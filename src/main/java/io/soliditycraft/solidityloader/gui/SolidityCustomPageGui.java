package io.soliditycraft.solidityloader.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CustomPagedSolidityGui} class provides a paginated GUI system where developers can
 * explicitly define custom pages, with each page represented by a {@link SolidityGui}.
 * It supports adding custom pages and navigating between them with "Next" and "Previous" buttons.
 */
public class SolidityCustomPageGui {

    /**
     * List of custom pages, each represented by a {@link SolidityGui}.
     */
    private final List<SolidityGui> pages = new ArrayList<>();

    /**
     * The current page index the player is viewing (0-indexed).
     */
    private int currentPage = 0;

    /**
     * Adds a new custom page to the GUI system.
     *
     * @param page the {@link SolidityGui} representing the custom page.
     * @return the current instance of {@code CustomPagedSolidityGui}, useful for method chaining.
     */
    public SolidityCustomPageGui addPage(SolidityGui page) {
        // Add navigation buttons to the page if necessary
        addNavigationButtons(page);
        pages.add(page);
        return this;
    }

    /**
     * Opens the current page of the GUI for the specified player and registers the GUI as a listener.
     *
     * @param player the player who will see the inventory.
     * @return the current instance of {@code CustomPagedSolidityGui}, useful for method chaining.
     */
    public SolidityCustomPageGui open(@NotNull Player player) {
        if (pages.isEmpty()) {
            throw new IllegalStateException("No pages added to the GUI.");
        }
        pages.get(currentPage).open(player); // Open the current page
        return this;
    }

    /**
     * Adds "Next" and "Previous" buttons to a given page if applicable.
     *
     * @param page the {@link SolidityGui} where the buttons will be added.
     */
    private void addNavigationButtons(SolidityGui page) {
        // "Next Page" button
        if (pages.size() > 1) {
            if (currentPage < pages.size() - 1) {
                ItemStack nextPage = createNavigationItem(Material.ARROW, "§aNext Page");
                page.addItem(new SolidityItem(nextPage, page.getSize() - 1, this::nextPage));
            }

            // "Previous Page" button
            if (currentPage > 0) {
                ItemStack prevPage = createNavigationItem(Material.ARROW, "§cPrevious Page");
                page.addItem(new SolidityItem(prevPage, page.getSize() - 9, this::previousPage));
            }
        }
    }

    /**
     * Creates a navigation {@link ItemStack} with the specified material and name.
     *
     * @param material the {@link Material} of the item (e.g., an arrow for navigation).
     * @param name     the display name of the item.
     * @return the created {@link ItemStack}.
     */
    private ItemStack createNavigationItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Moves to the next page in the GUI.
     *
     * @param player the player to whom the next page will be shown.
     */
    private void nextPage(Player player, InventoryClickEvent e) {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            open(player);
        }
    }

    /**
     * Moves to the previous page in the GUI.
     *
     * @param player the player to whom the previous page will be shown.
     */
    private void previousPage(Player player, InventoryClickEvent e) {
        if (currentPage > 0) {
            currentPage--;
            open(player);
        }
    }

    /**
     * Handles inventory click events, such as clicks on "Next" and "Previous" page buttons.
     *
     * @param event the {@link InventoryClickEvent} triggered when a player interacts with the GUI.
     */
    @EventHandler
    public void handleInventoryClick(@NotNull InventoryClickEvent event) {
        SolidityGui currentGui = pages.get(currentPage);
        currentGui.handleInventoryClick(event); // Delegate the event to the current page's handler
    }
}
