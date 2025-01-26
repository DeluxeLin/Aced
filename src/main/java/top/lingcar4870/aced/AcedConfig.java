package top.lingcar4870.aced;

import net.minecraftforge.common.ForgeConfigSpec;

public class AcedConfig {
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec.IntValue DEATH_INHERIT_MAX_STACK_SIZE;
    public static final ForgeConfigSpec.IntValue DEATH_INHERIT_CONTAINER_SIZE;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("DeathInherit");
        DEATH_INHERIT_CONTAINER_SIZE = builder.defineInRange("ContainerSize", 9, 1, 27);
        DEATH_INHERIT_MAX_STACK_SIZE = builder.defineInRange("MaxStackSize", 8, 1, 64);
        builder.pop();

        COMMON = builder.build();
    }
}
