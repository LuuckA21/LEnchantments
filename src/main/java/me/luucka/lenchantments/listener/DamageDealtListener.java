package me.luucka.lenchantments.listener;

import me.luucka.lcore.log.LLogger;
import me.luucka.lenchantments.effect.DamageDealtModifier;
import me.luucka.lenchantments.effect.DamageDealtReaction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class DamageDealtListener implements Listener {

	private final List<DamageDealtModifier> damageDealtModifiers;
	private final List<DamageDealtReaction> damageDealtReactions;

	public DamageDealtListener(final List<DamageDealtModifier> damageDealtModifiers, final List<DamageDealtReaction> damageDealtReactions) {
		this.damageDealtModifiers = List.copyOf(damageDealtModifiers);
		this.damageDealtReactions = List.copyOf(damageDealtReactions);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onModify(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player player)) {
			return;
		}

		if (!(event.getEntity() instanceof LivingEntity target)) {
			return;
		}

		if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
			return;
		}

		final ItemStack weapon = player.getInventory().getItemInMainHand();
		if (weapon.getType().isAir()) {
			return;
		}

		final double baseDamage = event.getDamage();
		if (baseDamage <= 0) {
			return;
		}

		double damage = baseDamage;

		for (final DamageDealtModifier damageDealtModifier : damageDealtModifiers) {
			final int level = weapon.getEnchantmentLevel(damageDealtModifier.enchantment());

			if (level >= 1) {
				damage = damageDealtModifier.modify(player, target, weapon, level, damage);
			}
		}

		if (!Double.isFinite(damage) || damage < 0) {
			return;   // un effetto ha fatto matematica sbagliata: non tocchiamo l'evento
		}

		if (damage != baseDamage) {
			event.setDamage(damage);
		}

		LLogger.debug("Damage dealt: {} -> {} | final: {}", baseDamage, damage, event.getFinalDamage());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onReact(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player player)) {
			return;
		}

		if (!(event.getEntity() instanceof LivingEntity target)) {
			return;
		}

		if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
			return;
		}

		final ItemStack weapon = player.getInventory().getItemInMainHand();
		if (weapon.getType().isAir()) {
			return;
		}

		final double finalDamage = event.getFinalDamage();
		if (finalDamage <= 0) {
			return;
		}

		for (final DamageDealtReaction damageDealtReaction : damageDealtReactions) {
			final int level = weapon.getEnchantmentLevel(damageDealtReaction.enchantment());

			if (level >= 1) {
				damageDealtReaction.react(player, target, weapon, level, finalDamage);
			}
		}
	}
}