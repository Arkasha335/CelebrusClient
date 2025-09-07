package com.celebrus.manager;

import com.celebrus.event.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

    // --- Annotation and Custom Event Definition ---

    // Наша собственная аннотация для методов-слушателей.
    // Мы назвали ее @Subscribe, чтобы не путать с @SubscribeEvent из Forge.
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Subscribe {
    }

    // Наше собственное событие, которое будет вызываться каждый игровой тик.
    // Модули будут "слушать" именно его, чтобы выполнять свою логику.
    public static class EventUpdate extends Event {}


    // --- Event Bus Logic ---

    // Хранилище для всех "подписок". Ключ - класс события, значение - список методов, которые его слушают.
    private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY = new HashMap<>();

    // Метод для регистрации объекта (например, модуля) в нашей системе событий
    public static void register(Object object) {
        for (final Method method : object.getClass().getDeclaredMethods()) {
            // Ищем методы с нашей аннотацией @Subscribe
            if (method.isAnnotationPresent(Subscribe.class)) {
                // Проверяем, что у метода только один параметр - наше событие
                if (method.getParameterTypes().length == 1) {
                    Class<?> eventClass = method.getParameterTypes()[0];

                    // Убеждаемся, что этот параметр наследуется от нашего базового класса Event
                    if (Event.class.isAssignableFrom(eventClass)) {
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }

                        Class<? extends Event> realEventClass = (Class<? extends Event>) eventClass;
                        
                        // Если для такого события еще нет списка подписчиков, создаем его
                        REGISTRY.computeIfAbsent(realEventClass, k -> new ArrayList<>());

                        // Добавляем метод в список подписчиков
                        REGISTRY.get(realEventClass).add(new MethodData(object, method));
                    }
                }
            }
        }
    }

    // Метод для отписки объекта
    public static void unregister(Object object) {
        for (List<MethodData> dataList : REGISTRY.values()) {
            dataList.removeIf(data -> data.getSource().equals(object));
        }
    }

    // Метод для вызова события. Все подписчики на это событие будут уведомлены.
    public static <T extends Event> T call(T event) {
        List<MethodData> dataList = REGISTRY.get(event.getClass());
        if (dataList != null) {
            for (MethodData data : dataList) {
                try {
                    data.getTarget().invoke(data.getSource(), event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return event;
    }


    // --- Helper Class ---

    // Вспомогательный класс для хранения пары (объект + его метод-слушатель)
    private static class MethodData {
        private final Object source;
        private final Method target;

        public MethodData(Object source, Method target) {
            this.source = source;
            this.target = target;
        }

        public Object getSource() { return source; }
        public Method getTarget() { return target; }
    }


    // --- Forge Event Listener ---

    // Этот метод слушает событие тика от Forge, используя его аннотацию @SubscribeEvent
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        // Мы вызываем наше собственное событие только в конце тика
        if (event.phase == TickEvent.Phase.END) {
            // Вызываем наше кастомное событие EventUpdate, на которое будут подписываться модули
            call(new EventUpdate());
        }
    }
}