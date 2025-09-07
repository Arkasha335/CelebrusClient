package com.celebrus.manager;

import com.celebrus.command.Command;
import com.celebrus.command.impl.BindCommand;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public List<Command> commands = new ArrayList<>();
    public final String prefix = ".";

    public CommandManager() {
        // Больше не регистрируемся в шине Forge, а регистрируемся в нашей
        EventManager.register(this);
        commands.add(new BindCommand());
    }

    // V--- ПОЛНОСТЬЮ НОВЫЙ МЕТОД ПЕРЕХВАТА ---V
    @EventManager.Subscribe
    public void onPacket(EventManager.EventPacket event) {
        // Проверяем, является ли пакет отправляемым сообщением в чат
        if (event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage chatPacket = (C01PacketChatMessage) event.getPacket();
            String message = chatPacket.getMessage();

            if (message.startsWith(prefix)) {
                // Отменяем отправку пакета на сервер
                event.setCancelled(true);
                Command.mc.ingameGUI.getChatGUI().addToSentMessages(message);

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
    }

    public static void addChatMessage(String message) {
        Command.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[Celebrus] " + EnumChatFormatting.GRAY + message));
    }
}