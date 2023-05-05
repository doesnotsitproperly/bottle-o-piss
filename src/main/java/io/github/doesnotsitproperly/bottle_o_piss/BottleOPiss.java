package io.github.doesnotsitproperly.bottle_o_piss;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BottleOPiss implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("piss")
			.then(argument("number", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
				.executes(ctx -> {
					ServerPlayerEntity player = ctx.getSource().getPlayer();
					if (player != null) {
						ItemStack bottleOPiss = new ItemStack(Items.SPLASH_POTION);
						NbtCompound bottleOPissNbt = new NbtCompound();
				
						bottleOPissNbt.putInt("CustomPotionColor", 16776960);
				
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
				})
			)
		));
	}
}
