package me.luucka.lenchantments.listener;

import me.luucka.lenchantments.effect.taken.BulwarkEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class BulwarkCleanupListener implements Listener {

	private final BulwarkEffect bulwark;

	public BulwarkCleanupListener(final BulwarkEffect bulwark) {
		this.bulwark = bulwark;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.bulwark.forget(event.getPlayer());
	}
}