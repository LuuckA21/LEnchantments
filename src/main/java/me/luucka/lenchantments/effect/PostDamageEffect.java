package me.luucka.lenchantments.effect;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PostDamageEffect {

	Enchantment enchantment();
	
	void apply(Player player, LivingEntity target, ItemStack weapon, int level, double finalDamage);

}