package me.luucka.lenchantments.registry;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.luucka.lenchantments.registry.registration.LEnchantmentRegistration;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LEnchantmentList {

	private static Map<TypedKey<Enchantment>, Enchantment> enchantments;

	public static synchronized void initialize() {
		if (enchantments != null) {
			return;
		}

		final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
		final Map<TypedKey<Enchantment>, Enchantment> loaded = new HashMap<>();

		for (LEnchantmentRegistration registration : LEnchantmentRegistration.values()) {
			TypedKey<Enchantment> key = registration.key();
			loaded.put(key, registry.getOrThrow(key));
		}

		enchantments = Map.copyOf(loaded);
	}

	/**
	 * Recupera un enchantment tramite la sua TypedKey.
	 */
	public static Enchantment get(TypedKey<Enchantment> key) {
		Objects.requireNonNull(key, "La chiave dell'enchantment non può essere null");

		Enchantment enchantment = getEnchantments().get(key);

		if (enchantment == null) {
			throw new IllegalArgumentException(
					"Enchantment non gestito da LEnchantmentList: " + key
			);
		}

		return enchantment;
	}

	private static Map<TypedKey<Enchantment>, Enchantment> getEnchantments() {
		if (enchantments == null) {
			throw new IllegalStateException("LEnchantmentList non è ancora stata inizializzata");
		}

		return enchantments;
	}

	public static boolean isInitialized() {
		return enchantments != null;
	}
}