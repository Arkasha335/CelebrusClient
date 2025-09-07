package com.celebrus;

import com.celebrus.manager.CommandManager;
import com.celebrus.manager.EventManager;
import com.celebrus.manager.ModuleManager;
import com.celebrus.ui.clickgui.ClickGui; // <--- ДОБАВЬ ЭТОТ IMPORT
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = Celebrus.MODID, version = Celebrus.VERSION, name = Celebrus.NAME)
public class Celebrus {
    // ... (существующие переменные)

    // Объявляем наши менеджеры
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ClickGui clickGui; // <--- ДОБАВЬ ЭТУ СТРОЧКУ

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // ... (существующая инициализация)

        // Инициализируем менеджеры в правильном порядке
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        clickGui = new ClickGui(); // <--- ДОБАВЬ ЭТУ СТРОЧКУ


        // Регистрируем наш EventManager в шине событий Forge, чтобы он мог слушать игровые события
        MinecraftForge.EVENT_BUS.register(new EventManager());

        // Устанавливаем кастомное название окна игры
        Display.setTitle(NAME + " " + VERSION);
    }
}