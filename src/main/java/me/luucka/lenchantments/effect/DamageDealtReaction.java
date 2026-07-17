package me.luucka.lenchantments.effect;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DamageDealtReaction {

	Enchantment enchantment();

	void react(Player player, LivingEntity target, ItemStack weapon, int level, double finalDamage);

}