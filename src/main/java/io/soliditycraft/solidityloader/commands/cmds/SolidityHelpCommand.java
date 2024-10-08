package io.soliditycraft.solidityloader.commands.cmds;

import io.soliditycraft.solidityloader.commands.ISolidityCMDExecutor;
import io.soliditycraft.solidityloader.commands.SolidityCommand;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import io.soliditycraft.solidityloader.utils.SolUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a command that shows help information for all registered commands in the Solidity system.
 * This class extends the SolidityCommand base class and provides an implementation for the onExecute method,
 * which handles pagination of the command list.
 */
public class SolidityHelpCommand extends SolidityCommand {

    private static final int COMMANDS_PER_PAGE = 5;  // Number of commands to display per page

    /**
     * Executes the help command and paginates the list of commands based on the provided arguments.
     *
     * @param sender  The sender of the command (e.g., a player or console).
     * @param command The command executor interface.
     * @param args    The list of arguments passed to the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean onExecute(SolidityCommandSender sender, ISolidityCMDExecutor command, @NotNull List<String> args) {
        List<SolidityCommand> commands = SolUtils.filter(getCommands(), (v) -> v.getPermission() == null || sender.hasPermission(v.getPermission()));
        List<String> cmds = SolUtils.map(
                commands,
                (v) -> "&6> - &5/solidity " + v.getName() + " " + v.getArgumentsUsage() + " &e| &6" + v.getDescription()
        );

        int totalPages = (int) Math.ceil((double) cmds.size() / COMMANDS_PER_PAGE);

        int page = 1;
        if (!args.isEmpty()) {
            try {
                page = Integer.parseInt(args.get(0));
                if (page < 1 || page > totalPages) {
                    sender.sendSolidityMessage("&cInvalid page number! Please choose a page between 1 and " + totalPages + ".");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendSolidityMessage("&cInvalid page number format! Please enter a valid number.");
                return true;
            }
        }

        int startIndex = (page - 1) * COMMANDS_PER_PAGE;
        int endIndex = Math.min(startIndex + COMMANDS_PER_PAGE, cmds.size());

        sender.sendMessage("&e&m" + " ".repeat(40));  // Adds a strikethrough line for visual separation

        sender.sendCenteredMessage("&6Help: Page " + page + " of " + totalPages);
        for (int i = startIndex; i < endIndex; i++) {
            sender.sendMessage(cmds.get(i));
        }

        TextComponent previousPage = new TextComponent("<< Previous");
        previousPage.setColor(ChatColor.GOLD);
        previousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/solidity help " + (page - 1)));

        TextComponent nextPage = new TextComponent("Next >>");
        nextPage.setColor(ChatColor.GOLD);
        nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/solidity help " + (page + 1)));

        // Add hover text to indicate what clicking will do
        previousPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click to view previous page").create()));
        nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click to view next page").create()));

        if (page > 1) {
            sender.sendMessage(previousPage);
        }

        if (page < totalPages) {
            sender.sendMessage(nextPage);
        }

        sender.sendMessage("&e&m" + " ".repeat(40));

        return true;
    }

    /**
     * Provides tab completion suggestions for this command.
     *
     * @param sender  The sender of the command.
     * @param command The command executor interface.
     * @param args    The list of arguments passed to the command.
     * @return A list of possible tab completions, or null if none.
     */
    @Override
    public List<String> onTabComplete(SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args) {
        return null;
    }

    /**
     * Gets the name of this command.
     *
     * @return The name of the command, which is typically used to register or execute the command.
     */
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Provides list of commands with their arguments and their description.";
    }

    @Override
    public String getArgumentsUsage() {
        return "[page]";
    }
}
