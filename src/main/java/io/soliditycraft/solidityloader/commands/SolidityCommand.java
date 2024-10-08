package io.soliditycraft.solidityloader.commands;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class SolidityCommand {

    private SolidityCommandManager commandManager;
    private SolidityLoader loader;

    public List<SolidityCommand> getCommands() {
        return commandManager.getCommands();
    }

    synchronized void initialize(SolidityLoader loader, SolidityCommandManager commandManager) {
        this.commandManager = commandManager;
        this.loader = loader;
    }

    public abstract boolean onExecute(SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args);

    public abstract List<String> onTabComplete(SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args);

    public abstract String getName();

    public String getDescription() {
        return "Managed by Solidity";
    }

    /**
     * Arguments usage for the command such as: required arg1, not required arg2
     *
     * @return
     */
    public String getArgumentsUsage() {
        return "";
    }

    public String getPermission() {
        return null;
    }

}
