package me.luucka.lenchantments.listener;

import me.luucka.lenchantments.effect.DamageModifyingEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class DamageModifyingEffectListener implements Listener {

	private final List<DamageModifyingEffect> effects;

	public DamageModifyingEffectListener(final List<DamageModifyingEffect> effects) {
		this.effects = List.copyOf(effects);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
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

		for (final DamageModifyingEffect effect : effects) {
			final int level = weapon.getEnchantmentLevel(effect.enchantment());

			if (level >= 1) {
				damage = effect.modify(player, target, weapon, level, damage);
			}
		}

		if (!Double.isFinite(damage) || damage < 0) {
			return;   // un effetto ha fatto matematica sbagliata: non tocchiamo l'evento
		}

		if (damage != baseDamage) {
			event.setDamage(damage);
		}
	}
}