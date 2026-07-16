package me.luucka.lenchantments.registry.registration;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryComposeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.Tag;
import me.luucka.lenchantments.effect.damagemodifying.ExecutionEffect;
import me.luucka.lenchantments.effect.postdamage.VampirismEffect;
import me.luucka.lenchantments.registry.LEnchantmentKeys;
import me.luucka.lenchantments.registry.LEnchantmentList;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;

public enum LEnchantmentRegistration implements EnchantmentRegistration {

	VAMPIRISM(LEnchantmentKeys.VAMPIRISM) {
		@Override
		public void configure(RegistryComposeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event, EnchantmentRegistryEntry.Builder builder) {
			Tag<ItemType> weapons = event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON);
			builder.description(Component.text("Vampirism"))
					.supportedItems(weapons)
					.primaryItems(weapons)
					.weight(2)
					.maxLevel(VampirismEffect.MAX_LEVEL)
					.minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(20, 8))
					.maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(50, 8))
					.anvilCost(8)
					.activeSlots(EquipmentSlotGroup.MAINHAND);
		}
	},

	EXECUTION(LEnchantmentKeys.EXECUTION) {
		@Override
		public void configure(RegistryComposeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event, EnchantmentRegistryEntry.Builder builder) {
			Tag<ItemType> weapons = event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON);
			builder.description(Component.text("Execution"))
					.supportedItems(weapons)
					.primaryItems(weapons)
					.weight(2)
					.maxLevel(ExecutionEffect.MAX_LEVEL)
					.minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(15, 9))
					.maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(45, 9))
					.anvilCost(4)
					.activeSlots(EquipmentSlotGroup.MAINHAND);
		}
	};

	private final TypedKey<Enchantment> key;

	public Enchantment enchantment() {
		return LEnchantmentList.get(this.key());
	}

	LEnchantmentRegistration(TypedKey<Enchantment> key) {
		this.key = key;
	}

	@Override
	public TypedKey<Enchantment> key() {
		return key;
	}

	public static void registerAll(RegistryComposeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
		for (LEnchantmentRegistration registration : values()) {
			registration.register(event);
		}
	}
}
