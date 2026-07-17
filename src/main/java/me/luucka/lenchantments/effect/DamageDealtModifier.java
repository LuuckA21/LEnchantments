package me.luucka.lenchantments.effect;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DamageDealtModifier {

	Enchantment enchantment();

	/**
	 * @param currentDamage danno base corrente, gia' modificato dagli effetti precedenti
	 * @return il nuovo danno base
	 */
	double modify(Player player, LivingEntity target, ItemStack weapon, int level, double currentDamage);

}