package com.celebrus.manager;

import com.celebrus.module.Module;
import com.celebrus.module.impl.combat.KillAura
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Здесь мы будем добавлять (регистрировать) все наши модули
        modules.add(new KillAura()); // <--- ДОБАВЬ ЭТУ СТРОЧКУ
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public List<Module> getModulesInCategory(Module.Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) {
                categoryModules.add(m);
            }
        }
        return categoryModules;
    }
}