package com.celebrus.manager;

import com.celebrus.command.Command;
import com.celebrus.command.impl.BindCommand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientSendMessageEvent; // <--- ИСПРАВЛЕН IMPORT
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public List<Command> commands = new ArrayList<>();
    public final String prefix = ".";

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
        commands.add(new BindCommand());
    }

    // V--- ИСПРАВЛЕНО СОБЫТИЕ ---V
    @SubscribeEvent
    public void onChatSent(ClientSendMessageEvent event) {
        String message = event.message;

        if (!message.startsWith(prefix)) {
            return;
        }

        event.setCanceled(true);
        Command.mc.ingameGUI.getChatGUI().addToSentMessages(message); // Добавляем команду в историю чата

        message = message.substring(prefix.length());
        String[] args = message.split(" ");

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

    public static void addChatMessage(String message) {
        Command.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[Celebrus] " + EnumChatFormatting.GRAY + message));
    }
}