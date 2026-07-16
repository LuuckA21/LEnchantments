package me.luucka.lenchantments.registry.registration;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryComposeEvent;
import org.bukkit.enchantments.Enchantment;

public interface EnchantmentRegistration {

	TypedKey<Enchantment> key();

	void configure(RegistryComposeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event, EnchantmentRegistryEntry.Builder builder);

	default void register(RegistryComposeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
		event.registry().register(key(), builder -> configure(event, builder));
	}

}
