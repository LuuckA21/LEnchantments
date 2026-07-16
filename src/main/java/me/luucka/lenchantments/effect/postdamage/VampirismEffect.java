package me.luucka.lenchantments.effect.postdamage;

import me.luucka.lenchantments.effect.PostDamageEffect;
import me.luucka.lenchantments.util.ChanceUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class VampirismEffect implements PostDamageEffect {

	/**
	 * @param activation probabilità di attivazione (0..1)
	 * @param healRatio  frazione del danno convertita in cura (0..1)
	 * @param maxHeal    cura massima per colpo, in punti vita (2 pv = 1 cuore)
	 */
	private record Tier(double activation, double healRatio, double maxHeal) {
	}

	// indice 0 = livello I
	private static final Tier[] TIERS = {
			new Tier(0.08, 0.10, 2.0),   // I    8%  | 10% | 2 pv   / 1 cuore
			new Tier(0.12, 0.15, 2.5),   // II   12% | 15% | 2.5 pv / 1.25 cuori
			new Tier(0.16, 0.20, 3.0),   // III  16% | 20% | 3 pv   / 1.5 cuori
			new Tier(0.20, 0.25, 4.0)    // IV   20% | 25% | 4 pv   / 2 cuori
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
	public void apply(final Player player,
	                  final LivingEntity target,
	                  final ItemStack weapon,
	                  final int level,
	                  final double finalDamage) {

		final Tier tier = tierFor(level);
		if (tier == null) return;

		if (!ChanceUtil.succeeds(tier.activation())) return;

		final double healed = Math.min(finalDamage * tier.healRatio(), tier.maxHeal());
		if (healed <= 0) return;

		player.heal(healed, EntityRegainHealthEvent.RegainReason.MAGIC);
	}

	private static @Nullable Tier tierFor(final int level) {
		if (level < 1) return null;
		return TIERS[Math.min(level, MAX_LEVEL) - 1];
	}
}