package com.celebrus.manager;

import com.celebrus.Celebrus;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatHandler {
    
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        // Обрабатываем команды из чата
        String message = event.message.getUnformattedText();
        if (Celebrus.instance != null && Celebrus.instance.commandManager != null) {
            Celebrus.instance.commandManager.processCommand(message);
        }
    }
} 