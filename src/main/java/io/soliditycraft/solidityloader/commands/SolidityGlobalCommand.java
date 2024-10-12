package io.soliditycraft.solidityloader.commands;

import org.bukkit.command.Command;

public abstract class SolidityGlobalCommand extends Command {

    protected SolidityGlobalCommand() {
        super("");
    }

    public abstract void initialize();

}
