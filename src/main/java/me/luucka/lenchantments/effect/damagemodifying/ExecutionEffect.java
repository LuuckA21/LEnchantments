package me.luucka.lenchantments.effect.damagemodifying;

import me.luucka.lenchantments.effect.DamageModifyingEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public final class ExecutionEffect implements DamageModifyingEffect {

	/**
	 * Frazione della vita massima sotto cui il bersaglio e' "eseguibile" (0..1).
	 */
	private static final double HEALTH_THRESHOLD = 0.30;

	/**
	 * Moltiplicatore del danno base. Indice 0 = livello I.
	 */
	private static final double[] MULTIPLIERS = {
			1.25,   // I
			1.45,   // II
			1.65    // III
	};

	public static final int MAX_LEVEL = MULTIPLIERS.length;

	private final Enchantment enchantment;

	public ExecutionEffect(final Enchantment enchantment) {
		this.enchantment = Objects.requireNonNull(enchantment, "enchantment");
	}

	@Override
	public Enchantment enchantment() {
		return this.enchantment;
	}

	@Override
	public double modify(final Player player,
	                     final LivingEntity target,
	                     final ItemStack weapon,
	                     final int level,
	                     final double currentDamage) {

		final double multiplier = multiplierFor(level);
		if (multiplier <= 1.0) return currentDamage;

		final AttributeInstance attribute = target.getAttribute(Attribute.MAX_HEALTH);
		if (attribute == null) return currentDamage;

		final double maxHealth = attribute.getValue();
		if (maxHealth <= 0) return currentDamage;

		// getHealth() qui e' la vita PRIMA che il colpo venga applicato
		if (target.getHealth() / maxHealth >= HEALTH_THRESHOLD) return currentDamage;

		return currentDamage * multiplier;
	}

	private static double multiplierFor(final int level) {
		if (level < 1) return 1.0;
		return MULTIPLIERS[Math.min(level, MAX_LEVEL) - 1];
	}
}