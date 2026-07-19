package me.luucka.lenchantments.registry;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LEnchantmentKeys {

	public static final String NAMESPACE = "lenchantments";

	public static final TypedKey<Enchantment> VAMPIRISM = create("vampirism");

	public static final TypedKey<Enchantment> EXECUTION = create("execution");

	public static final TypedKey<Enchantment> SECOND_WIND = create("second_wind");

	public static final TypedKey<Enchantment> BULWARK = create("bulwark");

	private static TypedKey<Enchantment> create(final String value) {
		return EnchantmentKeys.create(Key.key(NAMESPACE, value));
	}
}
