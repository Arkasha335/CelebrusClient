package com.celebrus;

import com.celebrus.manager.CommandManager;
import com.celebrus.manager.EventManager;
import com.celebrus.manager.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = "celebrus", version = "1.0", name = "Celebrus Client")
public class Celebrus {
    public static final String MODID = "celebrus";
    public static final String VERSION = "1.0";
    public static final String NAME = "Celebrus Client";

    public static Celebrus instance;

    public ModuleManager moduleManager;
    public CommandManager commandManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;
        
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        MinecraftForge.EVENT_BUS.register(new EventManager());

        Display.setTitle(NAME + " " + VERSION);
    }
}