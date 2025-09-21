package me.blueishberry.bluesutilityaddons.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;

public class BluesUtilityAddonsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Blue's Utlity Addons Config"));

            ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

            general.addEntry(builder.entryBuilder()
                    .startBooleanToggle(Text.of("Enable Toggle Sneak"), BluesUtilityConfig.INSTANCE.toggleSneak)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> {
                        BluesUtilityConfig.INSTANCE.toggleSneak = newValue;
                        BluesUtilityConfig.save();
                    })
                    .build());

            general.addEntry(builder.entryBuilder()
                    .startBooleanToggle(Text.of("Enable Quick Equip (TRIGGERS ANTICHEAT!)"), BluesUtilityConfig.INSTANCE.quickEquip)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> {
                        BluesUtilityConfig.INSTANCE.quickEquip = newValue;
                        BluesUtilityConfig.save();
                    })
                    .build());

            general.addEntry(builder.entryBuilder()
                    .startBooleanToggle(Text.of("Enable Replier"), BluesUtilityConfig.INSTANCE.replier)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> {
                        BluesUtilityConfig.INSTANCE.replier = newValue;
                        BluesUtilityConfig.save();
                    })
                    .build());

            return builder.build();
        };
    }


}
