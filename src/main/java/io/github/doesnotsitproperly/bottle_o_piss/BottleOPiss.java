package io.github.doesnotsitproperly.bottle_o_piss;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

public class BottleOPiss implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("piss").executes(ctx -> {
			ServerCommandSource source = ctx.getSource();
			ServerWorld world = source.getWorld();
			Vec3d position = source.getPosition();

			AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(world, position.x, position.y, position.z);
			areaEffectCloud.addEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0, false, false));
			areaEffectCloud.setColor(0xffff00);
			areaEffectCloud.setDuration(200);
			areaEffectCloud.setRadius(1.5f);

			world.spawnEntity(areaEffectCloud);

			return 1;
		})));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("pissbottles").then(CommandManager.argument("number", IntegerArgumentType.integer(1, Integer.MAX_VALUE)).executes(ctx -> {
			ServerPlayerEntity player = ctx.getSource().getPlayer();
			if (player != null) {
				ItemStack bottleOPiss = new ItemStack(Items.SPLASH_POTION);

				NbtCompound bottleOPissNbt = new NbtCompound();

				bottleOPissNbt.putInt("CustomPotionColor", 0xffff00);

				NbtCompound display = new NbtCompound();
				display.putString("Name", "[{\"text\":\"Bottle o\' Piss\",\"italic\":false,\"color\":\"yellow\"}]");
				bottleOPissNbt.put("display", display);

				NbtList customPotionEffects = new NbtList();
				NbtCompound effects = new NbtCompound();
				effects.putInt("Id", 9);
				effects.putInt("Duration", 600);
				customPotionEffects.add(effects);
				bottleOPissNbt.put("CustomPotionEffects", customPotionEffects);

				bottleOPiss.setNbt(bottleOPissNbt);

				PlayerInventory playerInventory = player.getInventory();
				for (int i = 0; i < IntegerArgumentType.getInteger(ctx, "number"); i++) {
					playerInventory.offerOrDrop(bottleOPiss.copy());
				}
			}
			return 1;
		}))));
	}
}
