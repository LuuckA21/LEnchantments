package me.luucka.lenchantments.listener;

import me.luucka.lenchantments.effect.PostDamageEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class PostDamageEffectListener implements Listener {

	private final List<PostDamageEffect> effects;

	public PostDamageEffectListener(final List<PostDamageEffect> effects) {
		this.effects = List.copyOf(effects);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
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

		final double finalDamage = event.getFinalDamage();
		if (finalDamage <= 0) {
			return;
		}

		for (final PostDamageEffect effect : effects) {
			final int level = weapon.getEnchantmentLevel(effect.enchantment());

			if (level >= 1) {
				effect.apply(player, target, weapon, level, finalDamage);
			}
		}
	}
}