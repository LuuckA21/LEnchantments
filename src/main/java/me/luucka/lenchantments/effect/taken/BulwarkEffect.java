package me.luucka.lenchantments.effect.taken;

import me.luucka.lcore.log.LLogger;
import me.luucka.lenchantments.effect.DamageTakenModifier;
import me.luucka.lenchantments.util.StackTracker;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class BulwarkEffect implements DamageTakenModifier {

	/**
	 * Riduzione per stack e tetto di stack, per livello. Indice 0 = livello I.
	 */
	private record Tier(double reductionPerStack, int maxStacks) {
	}

	private static final Tier[] TIERS = {
			new Tier(0.08, 3),   // I    8% x3  -> tetto 24%
			new Tier(0.10, 4),   // II   10% x4 -> tetto 40%
			new Tier(0.12, 4)    // III  12% x4 -> tetto 48%
	};

	public static final int MAX_LEVEL = TIERS.length;
	public static final long WINDOW_MILLIS = 2_000L;

	/**
	 * Un tracker per livello: il tetto di stack dipende dal livello.
	 */
	private final StackTracker[] trackers;
	private final Enchantment enchantment;

	public BulwarkEffect(final Enchantment enchantment) {
		this.enchantment = Objects.requireNonNull(enchantment, "enchantment");
		this.trackers = new StackTracker[TIERS.length];
		for (int i = 0; i < TIERS.length; i++) {
			this.trackers[i] = new StackTracker(WINDOW_MILLIS, TIERS[i].maxStacks());
		}
	}

	@Override
	public Enchantment enchantment() {
		return this.enchantment;
	}

	@Override
	public double modify(final Player player, final int level, final double currentDamage) {
		final int index = Math.clamp(level, 1, MAX_LEVEL) - 1;
		final Tier tier = TIERS[index];

		final int activeStacks = this.trackers[index].hit(player);
		if (activeStacks <= 0) return currentDamage;

		final double reduction = activeStacks * tier.reductionPerStack();
		LLogger.debug("Bulwark {}: {} - {} * {} = {}", level, player.getName(), currentDamage, reduction, currentDamage * (1.0 - reduction));
		return currentDamage * (1.0 - reduction);
	}

	public void forget(final Player player) {
		for (final StackTracker tracker : this.trackers) {
			tracker.clear(player);
		}
	}
}