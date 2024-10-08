package io.soliditycraft.solidityloader.sender;

import io.soliditycraft.solidityloader.utils.DefaultFontInfo;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
public class SolidityCommandSender {

    private final static int CENTER_PX = 154;
    private final CommandSender sender;


    public SolidityCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull SolidityCommandSender from(CommandSender sender) {
        return new SolidityCommandSender(sender);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull SolidityCommandSender from(Player player) {
        return new SolidityCommandSender(player);
    }

    public void sendMessage(String message) {
        String content = ChatColor.translateAlternateColorCodes('&', message);
        this.sender.sendMessage(content);
    }

    public void sendMessage(BaseComponent component) {
        this.sender.spigot().sendMessage(component);
    }

    public void sendSolidityMessage(String message) {
        sendMessage("&5&lSolidity > &r" + message);
    }

    public boolean hasPermission(String name) {
        return this.getSender().hasPermission(name);
    }

    public boolean hasPermission(Permission permission) {
        return this.getSender().hasPermission(permission);
    }


    public void sendCenteredMessage(String message) {
        if (message == null || message.equals("")) {
            this.sendMessage("");
            return;
        }

        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        this.sender.sendMessage(sb + message);
    }

}
