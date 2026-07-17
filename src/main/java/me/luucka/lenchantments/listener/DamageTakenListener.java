package me.luucka.lenchantments.listener;

import me.luucka.lenchantments.effect.DamageTakenReaction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DamageTakenListener implements Listener {

	private final Plugin plugin;
	private final List<DamageTakenReaction> reactions;

	public DamageTakenListener(final Plugin plugin, final List<DamageTakenReaction> reactions) {
		this.plugin = plugin;
		this.reactions = List.copyOf(reactions);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}

		if (event.getFinalDamage() <= 0) {
			return;
		}

		final EntityEquipment equipment = player.getEquipment();
		if (equipment.getArmorContents().length == 0) {
			return;
		}

		// i livelli si leggono ADESSO: tra un tick l'armatura potrebbe rompersi o cambiare
		final Map<DamageTakenReaction, Integer> triggered = new LinkedHashMap<>();

		for (final DamageTakenReaction reaction : this.reactions) {
			final int level = armorLevel(equipment, reaction.enchantment());
			if (level >= 1) {
				triggered.put(reaction, level);
			}
		}

		if (triggered.isEmpty()) {
			return;
		}

		// un tick dopo, getHealth() riflette il colpo
		this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
			if (!player.isOnline() || player.isDead() || player.getHealth() <= 0) {
				return;
			}
			triggered.forEach((reaction, level) -> reaction.react(player, level));
		});
	}

	/**
	 * Il livello piu' alto tra i 4 pezzi: non si sommano.
	 */
	private static int armorLevel(final EntityEquipment equipment, final Enchantment enchantment) {
		int max = 0;
		for (final ItemStack piece : equipment.getArmorContents()) {
			if (piece == null || piece.getType().isAir()) continue;
			max = Math.max(max, piece.getEnchantmentLevel(enchantment));
		}
		return max;
	}
}