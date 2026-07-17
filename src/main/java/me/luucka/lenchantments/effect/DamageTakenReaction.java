package me.luucka.lenchantments.effect;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public interface DamageTakenReaction {

	Enchantment enchantment();

	/**
	 * Invocato UN TICK DOPO il danno: player.getHealth() riflette gia' il colpo.
	 * Il giocatore e' garantito online e vivo.
	 */
	void react(Player player, int level);

}