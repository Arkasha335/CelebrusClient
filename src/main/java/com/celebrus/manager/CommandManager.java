package com.celebrus.manager;

import com.celebrus.command.Command;
import com.celebrus.command.impl.BindCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public List<Command> commands = new ArrayList<>();
    public final String prefix = ".";

    public CommandManager() {
        commands.add(new BindCommand());
    }

    // Обработка команд через консоль (для тестирования)
    public void processCommand(String message) {
        if (message.startsWith(prefix)) {
            String commandStr = message.substring(prefix.length());
            String[] args = commandStr.split(" ");

            for (Command command : commands) {
                if (command.getName().equalsIgnoreCase(args[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(args[0]))) {
                    try {
                        command.execute(Arrays.copyOfRange(args, 1, args.length));
                    } catch (Exception e) {
                        addChatMessage(EnumChatFormatting.RED + "Invalid command usage.");
                    }
                    return;
                }
            }
            addChatMessage(EnumChatFormatting.RED + "Command not found.");
        }
    }

    public static void addChatMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[Celebrus] " + EnumChatFormatting.GRAY + message));
        }
    }
}