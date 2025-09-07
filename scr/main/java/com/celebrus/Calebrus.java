package com.celebrus;

import com.celebrus.manager.CommandManager;
import com.celebrus.manager.EventManager;
import com.celebrus.manager.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = Celebrus.MODID, version = Celebrus.VERSION, name = Celebrus.NAME)
public class Celebrus {
    public static final String MODID = "celebrus";
    public static final String VERSION = "1.0";
    public static final String NAME = "Celebrus Client";

    // Создаем экземпляр нашего клиента, чтобы к нему можно было обращаться из любого места
    public static Celebrus instance;

    // Объявляем наши менеджеры
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    // EventManager будет статическим, поэтому экземпляр не нужен

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Инициализируем экземпляр
        instance = this;

        // Инициализируем менеджеры в правильном порядке
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        // Регистрируем наш EventManager в шине событий Forge, чтобы он мог слушать игровые события
        MinecraftForge.EVENT_BUS.register(new EventManager());

        // Устанавливаем кастомное название окна игры
        Display.setTitle(NAME + " " + VERSION);
    }
}