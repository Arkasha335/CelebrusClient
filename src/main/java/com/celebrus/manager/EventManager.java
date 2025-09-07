package com.celebrus.manager;

import com.celebrus.Celebrus;
import com.celebrus.event.Event;
import com.celebrus.ui.clickgui.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    // --- Аннотации и кастомные события ---
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Subscribe {}

    public static class EventUpdate extends Event {}

    // НОВОЕ СОБЫТИЕ ДЛЯ ПАКЕТОВ
    public static class EventPacket extends Event {
        private Packet<?> packet;
        public EventPacket(Packet<?> packet) { this.packet = packet; }
        public Packet<?> getPacket() { return packet; }
        public void setPacket(Packet<?> packet) { this.packet = packet; }
    }

    // --- Логика шины событий ---
    private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY = new HashMap<>();

    public static void register(Object object) {
        for (final Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterTypes().length == 1) {
                Class<?> eventClass = method.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(eventClass)) {
                    if (!method.isAccessible()) method.setAccessible(true);
                    Class<? extends Event> realEventClass = (Class<? extends Event>) eventClass;
                    REGISTRY.computeIfAbsent(realEventClass, k -> new ArrayList<>()).add(new MethodData(object, method));
                }
            }
        }
    }

    public static void unregister(Object object) {
        REGISTRY.values().forEach(list -> list.removeIf(data -> data.getSource().equals(object)));
    }

    public static <T extends Event> T call(T event) {
        List<MethodData> dataList = REGISTRY.get(event.getClass());
        if (dataList != null) {
            for (MethodData data : dataList) {
                try {
                    data.getTarget().invoke(data.getSource(), event);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        return event;
    }

    // --- Вспомогательный класс ---
    private static class MethodData {
        private final Object source;
        private final Method target;
        public MethodData(Object source, Method target) { this.source = source; this.target = target; }
        public Object getSource() { return source; }
        public Method getTarget() { return target; }
    }

    // --- Слушатели событий Forge ---
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            call(new EventUpdate());
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int keyCode = Keyboard.getEventKey();
            if (keyCode == Keyboard.KEY_RSHIFT) {
                Minecraft.getMinecraft().displayGuiScreen(new ClickGui());
            }
            if (Celebrus.instance != null && Celebrus.instance.moduleManager != null) {
                Celebrus.instance.moduleManager.modules.stream()
                        .filter(module -> module.getKeyBind() == keyCode)
                        .forEach(module -> module.toggle());
            }
        }
    }
}