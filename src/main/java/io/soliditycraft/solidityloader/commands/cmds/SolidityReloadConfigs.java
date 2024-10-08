package io.soliditycraft.solidityloader.commands.cmds;

import io.soliditycraft.solidityloader.commands.ISolidityCMDExecutor;
import io.soliditycraft.solidityloader.commands.SolidityCommand;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SolidityReloadConfigs extends SolidityCommand {
    @Override
    public boolean onExecute(@NotNull SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args) {

        this.getLoader().reloadConfig();
        sender.sendSolidityMessage("&aSuccessfully reloaded configurations!");

        return true;
    }

    @Override
    public List<String> onTabComplete(SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args) {
        return null;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads all Solidity configurations.";
    }

    @Override
    public String getPermission() {
        return "solidity.reload";
    }
}
