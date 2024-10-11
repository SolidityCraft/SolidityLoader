package io.soliditycraft.solidityloader.commands.cmds;

import io.soliditycraft.solidityloader.Constants;
import io.soliditycraft.solidityloader.commands.ISolidityCMDExecutor;
import io.soliditycraft.solidityloader.commands.SolidityCommand;
import io.soliditycraft.solidityloader.commands.SolidityCommandManager;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import io.soliditycraft.solidityloader.utils.SolUtils;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BaseSolidityCommand implements ISolidityCMDExecutor {

    @Getter
    private final SolidityCommandManager solidityCommandManager;

    public BaseSolidityCommand(SolidityCommandManager commandManager) {
        this.solidityCommandManager = commandManager;
    }

    public List<SolidityCommand> getCommands() {
        return solidityCommandManager.getCommands();
    }

    public String getErrorMessage() {
        return "&6Please run /solidity help for more information!";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> argv = SolUtils.toList(args);
        SolidityCommandSender commandSender = SolidityCommandSender.from(sender);
        if (argv.isEmpty()) {
            commandSender.sendSolidityMessage(getErrorMessage());
            return false;
        }

        String subcommand = argv.remove(0);
        SolidityCommand cmd = SolUtils.find(getCommands(), (v) -> v.getName().equals(subcommand));
        if (cmd == null) {
            commandSender.sendSolidityMessage(getErrorMessage());
            return false;
        }

        if (cmd.getPermission() != null) {
            if (!commandSender.hasSolidityPermission(cmd.getPermission()) || !commandSender.hasSolidityPermission("*")) {
                commandSender.sendSolidityMessage(Constants.NO_PERMISSIONS);
                return false;
            }
        }

        boolean result = cmd.onExecute(commandSender, this, argv);

        return result;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> argv = SolUtils.toList(strings);
        SolidityCommandSender sender = SolidityCommandSender.from(commandSender);
        List<SolidityCommand> cmds = SolUtils.filter(getCommands(), (v) -> v.getPermission() == null || sender.hasPermission(v.getPermission()));
        List<String> mappedCmds = SolUtils.map(cmds, (v) -> v.getName());
        if (argv.isEmpty()) {
            return mappedCmds;
        }

        String subcommand = argv.remove(0);
        SolidityCommand cmd = SolUtils.find(getCommands(), (v) -> v.getName().equals(subcommand));
        if (cmd == null) {
            return mappedCmds;
        }

        return cmd.onTabComplete(sender, this, argv);
    }
}
