package me.luucka.lenchantments.effect.dealt;

import me.luucka.lcore.log.LLogger;
import me.luucka.lenchantments.effect.DamageDealtModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public final class ExecutionEffect implements DamageDealtModifier {

	/**
	 * Frazione della vita massima sotto cui il bersaglio e' "eseguibile" (0..1).
	 */
	private static final double HEALTH_THRESHOLD = 0.30;

	/**
	 * Moltiplicatore del danno base. Indice 0 = livello I.
	 */
	private static final double[] MULTIPLIERS = {
			1.60,   // I
			2.05,   // II
			2.50    // III
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
	public double modify(final Player player, final LivingEntity target, final ItemStack weapon, final int level, final double currentDamage) {
		final double multiplier = multiplierFor(level);
		if (multiplier <= 1.0) return currentDamage;

		final AttributeInstance attribute = target.getAttribute(Attribute.MAX_HEALTH);
		if (attribute == null) return currentDamage;

		final double maxHealth = attribute.getValue();
		if (maxHealth <= 0) return currentDamage;

		// i cuori gialli contano: chi ha appena mangiato una mela d'oro non e' eseguibile.
		// getHealth() e' la vita PRIMA che il colpo venga applicato.
		final double effectiveHealth = target.getHealth() + target.getAbsorptionAmount();

		if (effectiveHealth / maxHealth >= HEALTH_THRESHOLD) return currentDamage;

		LLogger.debug("Execution {}: {} - {} * {} = {}", level, player.getName(), currentDamage, multiplier, (currentDamage * multiplier));
		return currentDamage * multiplier;
	}

	private static double multiplierFor(final int level) {
		if (level < 1) return 1.0;
		return MULTIPLIERS[Math.min(level, MAX_LEVEL) - 1];
	}
}