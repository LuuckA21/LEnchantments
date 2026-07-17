package me.luucka.lenchantments.effect.dealt;

import me.luucka.lenchantments.effect.DamageDealtReaction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class VampirismEffect implements DamageDealtReaction {

	/**
	 * @param healRatio frazione del danno convertita in cura (0..1)
	 * @param maxHeal   cura massima per colpo, in punti vita (2 pv = 1 cuore)
	 */
	private record Tier(double healRatio, double maxHeal) {
	}

	// indice 0 = livello I
	private static final Tier[] TIERS = {
			new Tier(0.04, 1.0),   // I    4%  | max 1.0 pv / 0.5 cuori
			new Tier(0.06, 1.5),   // II   6%  | max 1.5 pv / 0.75 cuori
			new Tier(0.08, 2.0),   // III  8%  | max 2.0 pv / 1 cuore
			new Tier(0.10, 3.0)    // IV   10% | max 3.0 pv / 1.5 cuori
	};

	public static final int MAX_LEVEL = TIERS.length;

	private final Enchantment enchantment;

	public VampirismEffect(final Enchantment enchantment) {
		this.enchantment = Objects.requireNonNull(enchantment, "enchantment");
	}

	@Override
	public Enchantment enchantment() {
		return this.enchantment;
	}

	@Override
	public void react(final Player player,
	                  final LivingEntity target,
	                  final ItemStack weapon,
	                  final int level,
	                  final double finalDamage) {

		final Tier tier = tierFor(level);
		if (tier == null) return;

		final double healed = Math.min(finalDamage * tier.healRatio(), tier.maxHeal());
		if (healed <= 0) return;

		player.heal(healed, EntityRegainHealthEvent.RegainReason.MAGIC);
	}

	private static @Nullable Tier tierFor(final int level) {
		if (level < 1) return null;
		return TIERS[Math.min(level, MAX_LEVEL) - 1];
	}
}