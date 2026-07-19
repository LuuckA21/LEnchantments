package me.luucka.lenchantments.effect;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public interface DamageTakenModifier {

	Enchantment enchantment();

	double modify(final Player player, final int level, final double currentDamage);
}
