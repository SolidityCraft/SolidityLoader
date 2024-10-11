package io.soliditycraft.solidityloader.commands.cmds;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.addons.SolidityAddon;
import io.soliditycraft.solidityloader.addons.SolidityAddonData;
import io.soliditycraft.solidityloader.commands.ISolidityCMDExecutor;
import io.soliditycraft.solidityloader.commands.SolidityCommand;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import io.soliditycraft.solidityloader.utils.PaginationHelper;
import io.soliditycraft.solidityloader.utils.SolUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SolidityAddonsCommand extends SolidityCommand {
    @Override
    public boolean onExecute(SolidityCommandSender sender, ISolidityCMDExecutor command, @NotNull List<String> args) {

        if (args.isEmpty()) {
            sendHelpMessage(sender);
            return false;
        }

        String subcommand = args.remove(0);

        if (subcommand.equals("help")) {
            sendHelpMessage(sender);
        } else if (subcommand.equals("info")) {
            String addonName = args.remove(0);
            if (addonName == null) {
                sendHelpMessage(sender);
                return false;
            }
            SolidityAddon addon = getLoader().getAddonManager().getAddon(addonName);
            if (addon == null) {
                sender.sendSolidityMessage("&cInvalid Solidity Addon!");
                return false;
            }
            SolidityAddonData data = addon.getInfo();

            sender.sendMessage("&e&m" + " ".repeat(40));
            sender.sendCenteredMessage("&6Solidity Addon: &a" + data.getName() + " &e(&a" + data.getId() + "&e)");
            sender.sendMessage("");
            sender.sendSolidityMessage(" &e&m>&r &eName: &a" + data.getName());
            sender.sendSolidityMessage(" &e&m>&r &eID: &a" + data.getId());
            sender.sendSolidityMessage(" &e&m>&r &eVersion: &a" + data.getVersion());
            sender.sendSolidityMessage(" &e&m>&r &eDescription: &a" + (data.getDescription() != null ? data.getDescription() : "Solidity Addon"));
            sender.sendSolidityMessage(" &e&m>&r &eSpigot Plugin?: " + (data.isLoadPlugin() ? "&aYes" : "&cNo"));
            sender.sendMessage("");
            sender.sendMessage("&e&m" + " ".repeat(40));
        } else if (subcommand.equals("disable")) {
            String addonName = args.remove(0);
            if (addonName == null) {
                sendHelpMessage(sender);
                return false;
            }
            SolidityAddon addon = getLoader().getAddonManager().getAddon(addonName);
            if (addon == null) {
                sender.sendSolidityMessage("&cInvalid Solidity Addon!");
                return false;
            }
            getLoader().getAddonManager().disableAddon(addonName);
        } else if (subcommand.equals("enable")) {
            String addonName = args.remove(0);
            if (addonName == null) {
                sendHelpMessage(sender);
                return false;
            }

            if (!getLoader().getAddonManager().isLoaded(addonName)) {
                sender.sendSolidityMessage("&cInvalid Solidity Addon!");
                return false;
            }

            getLoader().getAddonManager().enableAddon(addonName);
        } else if (subcommand.equals("load")) {
            String addonName = args.remove(0);
            if (addonName == null) {
                sendHelpMessage(sender);
                return false;
            }

            File file = new File(SolidityLoader.SOLIDITY_ADDON_FOLDER, addonName);
            if (!file.exists()) {
                sender.sendSolidityMessage("&cInvalid Solidity Addon!");
                return false;
            }

            try {
                getLoader().getAddonManager().loadAddon(file);
            } catch (Exception e) {
                sender.sendSolidityMessage("&cCouldn't load the Solidity Addon: " + addonName);
            }
        } else if (subcommand.equals("unload")) {
            String addonName = args.remove(0);
            if (addonName == null) {
                sendHelpMessage(sender);
                return false;
            }

            if (!getLoader().getAddonManager().isLoaded(addonName)) {
                sender.sendSolidityMessage("&cInvalid Solidity Addon!");
                return false;
            }

            getLoader().getAddonManager().unloadAddon(addonName);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(SolidityCommandSender sender, ISolidityCMDExecutor command, List<String> args) {
        return SolUtils.createEmptyList();
    }

    @Override
    public String getName() {
        return "addons";
    }

    @Override
    public String getDescription() {
        return "Manages addons which are added to Solidity.";
    }

    @Override
    public String getPermission() {
        return "addons";
    }

    public void sendHelpMessage(@NotNull SolidityCommandSender sender) {
        List<String> cmds = new ArrayList<>();
        cmds.add("info <addon> | Displays info about an addon.");
        cmds.add("disable <addon> | Disables an addon");
        cmds.add("enable <addon> | Enables an addon");
        cmds.add("load <addon_file> | Loads an addon (from plugins/SolidityLoader/addons)");
        cmds.add("unload <addon> | Unloads an addon");

        List<String> mappedCmds = SolUtils.map(cmds, (v) -> "/solidity addons " + v);
        PaginationHelper<String> helper = new PaginationHelper<>(mappedCmds, 5);
        int totalPages = helper.getTotalPages();
        List<String> page = helper.getPage(1);

        sender.sendMessage("&e&m" + " ".repeat(40));
        sender.sendCenteredMessage("&6Addons Help: Page " + page + " of " + totalPages);

        for (String p : page) {
            sender.sendSolidityMessage(p);
        }

        sender.sendMessage("&e&m" + " ".repeat(40));
    }
}
