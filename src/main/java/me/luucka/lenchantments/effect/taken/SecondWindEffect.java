package me.luucka.lenchantments.effect.taken;

import me.luucka.lenchantments.effect.DamageTakenReaction;
import me.luucka.lenchantments.util.CooldownStore;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class SecondWindEffect implements DamageTakenReaction {

	/**
	 * Frazione della vita massima sotto cui l'effetto scatta (0..1).
	 */
	private static final double HEALTH_THRESHOLD = 0.40;

	private static final int SHIELD_DURATION_TICKS = 600;   // 30s
	private static final int SPEED_DURATION_TICKS = 100;    // 5s

	/**
	 * @param absorptionAmplifier ampl. 0 = Assorbimento I = 4 pv
	 * @param speedAmplifier      ampl. 0 = Velocita' I
	 * @param cooldownMillis      tempo di ricarica
	 */
	private record Tier(int absorptionAmplifier, int speedAmplifier, long cooldownMillis) {
	}

	// indice 0 = livello I
	private static final Tier[] TIERS = {
			new Tier(0, 0, 180_000),   // I    4 pv  | Velocita' I  | 3 min
			new Tier(1, 0, 150_000),   // II   8 pv  | Velocita' I  | 2.5 min
			new Tier(2, 1, 120_000)    // III  12 pv | Velocita' II | 2 min
	};

	public static final int MAX_LEVEL = TIERS.length;

	private final Enchantment enchantment;
	private final CooldownStore cooldowns;

	public SecondWindEffect(final Enchantment enchantment, final CooldownStore cooldowns) {
		this.enchantment = Objects.requireNonNull(enchantment, "enchantment");
		this.cooldowns = Objects.requireNonNull(cooldowns, "cooldowns");
	}

	@Override
	public Enchantment enchantment() {
		return this.enchantment;
	}

	@Override
	public void react(final Player player, final int level) {
		final Tier tier = tierFor(level);
		if (tier == null) return;

		final AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
		if (attribute == null) return;

		final double maxHealth = attribute.getValue();
		if (maxHealth <= 0) return;

		// stessa formula di Execution: i cuori gialli contano
		final double effectiveHealth = player.getHealth() + player.getAbsorptionAmount();
		if (effectiveHealth / maxHealth >= HEALTH_THRESHOLD) return;

		// il cooldown si consuma SOLO se l'effetto scatta davvero
		if (!this.cooldowns.tryUse(player, tier.cooldownMillis())) return;

		player.addPotionEffect(new PotionEffect(
				PotionEffectType.ABSORPTION, SHIELD_DURATION_TICKS, tier.absorptionAmplifier()));
		player.addPotionEffect(new PotionEffect(
				PotionEffectType.SPEED, SPEED_DURATION_TICKS, tier.speedAmplifier()));
	}

	private static @Nullable Tier tierFor(final int level) {
		if (level < 1) return null;
		return TIERS[Math.min(level, MAX_LEVEL) - 1];
	}
}