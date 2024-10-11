package io.soliditycraft.solidityloader.commands.cmds;

import io.soliditycraft.solidityloader.commands.ISolidityCMDExecutor;
import io.soliditycraft.solidityloader.commands.SolidityCommand;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import io.soliditycraft.solidityloader.utils.PaginationHelper;
import io.soliditycraft.solidityloader.utils.SolUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SolidityHelpCommand extends SolidityCommand {

    private static final int COMMANDS_PER_PAGE = 5;

    @Override
    public boolean onExecute(SolidityCommandSender sender, ISolidityCMDExecutor command, @NotNull List<String> args) {
        List<SolidityCommand> commands = SolUtils.filter(getCommands(), (v) -> v.getPermission() == null || sender.hasPermission(v.getPermission()));
        List<String> cmds = SolUtils.map(
                commands,
                (v) -> "&6> - &5/solidity " + v.getName() + " " + v.getArgumentsUsage() + " &e| &6" + v.getDescription()
        );

        PaginationHelper<String> paginator = new PaginationHelper<>(cmds, COMMANDS_PER_PAGE);
        int totalPages = paginator.getTotalPages();

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

        List<String> pageCommands = paginator.getPage(page);

        sender.sendMessage("&e&m" + " ".repeat(40));  // Adds a strikethrough line for visual separation
        sender.sendCenteredMessage("&6Help: Page " + page + " of " + totalPages);

        for (String cmd : pageCommands) {
            sender.sendMessage(cmd);
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

    @Override
    public List<String> onTabComplete(SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args) {
        return null;
    }

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
