package com.celebrus.command.impl;

import com.celebrus.Celebrus;
import com.celebrus.command.Command;
import com.celebrus.manager.CommandManager;
import com.celebrus.module.Module;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "b");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            CommandManager.addChatMessage(EnumChatFormatting.RED + "Usage: .bind <module> <key>");
            return;
        }

        String moduleName = args[0];
        String keyName = args[1];

        Module module = Celebrus.instance.moduleManager.getModule(moduleName);

        if (module == null) {
            CommandManager.addChatMessage(EnumChatFormatting.RED + "Module '" + moduleName + "' not found.");
            return;
        }

        if (keyName.equalsIgnoreCase("none")) {
            module.setKeyBind(Keyboard.KEY_NONE);
            CommandManager.addChatMessage("Unbound " + module.getName() + ".");
            return;
        }

        int key = Keyboard.getKeyIndex(keyName.toUpperCase());

        if (key == Keyboard.KEY_NONE) {
            CommandManager.addChatMessage(EnumChatFormatting.RED + "Key '" + keyName + "' not found.");
            return;
        }

        module.setKeyBind(key);
        CommandManager.addChatMessage("Bound " + module.getName() + " to " + Keyboard.getKeyName(key) + ".");
    }
}