package ru.vladislemon.neutralmobs.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.vladislemon.neutralmobs.NeutralMobs;

import java.lang.reflect.Field;

@Mixin(NearestAttackableTargetGoal.class)
public class NearestAttackableTargetGoalMixin<T> {
    private static final Field MOB_FIELD = ObfuscationReflectionHelper.findField(TargetGoal.class, "f_26135_"); //mob

    @Shadow
    @Final
    protected Class<T> targetType;

    @Inject(method = "findTarget()V", at = @At("HEAD"), cancellable = true)
    private void findTarget(CallbackInfo callbackInfo) throws IllegalAccessException {
        if (targetType == Player.class || targetType == ServerPlayer.class) {
            Mob mob = (Mob) MOB_FIELD.get(this);
            if (NeutralMobs.getInstance().getConfig().getNeutralMobs().contains(mob.getType())) {
                callbackInfo.cancel();
            }
        }
    }
}
