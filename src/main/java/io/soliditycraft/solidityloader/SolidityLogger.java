package io.soliditycraft.solidityloader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SolidityLogger {

    @Contract(value = " -> new", pure = true)
    public static @NotNull SolidityLogger getLogger() {
        return new SolidityLogger();
    }


    public void log(String message) {
        message = "&5[Solidity]&r " + message;
        String ch = ChatColor.translateAlternateColorCodes('&', message);
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ch);
    }

    public void info(String message) {
        log("[&aINFO&r]: " + message);
    }

    public void warn(String message) {
        log("[&eWARN&r]: " + message);
    }

    public void error(String message) {
        log("[&cERROR&r]: " + message);
    }

    public void debug(String message) {
        log("[&5DEBUG&r]: " + message);
    }

    public void line(int length) {
        StringBuilder builder = new StringBuilder().append("&m");
        for (int i = 0; i < length; i++) {
            builder.append(" ");
        }
        log(builder.toString());
    }

    public void empty() {
        log("");
    }
}
