package com.celebrus.manager;

import com.celebrus.command.Command;
import com.celebrus.command.impl.BindCommand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public List<Command> commands = new ArrayList<>();
    public final String prefix = ".";

    public CommandManager() {
        // Регистрируем менеджер в шине событий Forge, чтобы слушать сообщения чата
        MinecraftForge.EVENT_BUS.register(this);
        // Добавляем наши команды
        commands.add(new BindCommand());
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();

        if (!message.startsWith(prefix)) {
            return;
        }

        // Отменяем отображение нашего сообщения в чате
        event.setCanceled(true);

        // Убираем префикс и разделяем сообщение на аргументы
        message = message.substring(prefix.length());
        String[] args = message.split(" ");

        // Ищем команду
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(args[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(args[0]))) {
                try {
                    // Выполняем команду, передавая ей аргументы (без самой команды)
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
        // Helper-метод для отправки сообщений в чат клиента
        Command.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[Celebrus] " + EnumChatFormatting.GRAY + message));
    }
}