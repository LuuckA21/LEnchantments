package me.luucka.lenchantments.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CooldownStore {

	private final Map<UUID, Long> expiry = new HashMap<>();

	/**
	 * Consuma il cooldown se disponibile.
	 *
	 * @return true se l'azione puo' procedere (e il cooldown e' stato riarmato)
	 */
	public boolean tryUse(final Player player, final long durationMillis) {
		final long now = System.currentTimeMillis();
		final UUID id = player.getUniqueId();
		final Long until = this.expiry.get(id);

		if (until != null && until > now) return false;

		this.expiry.put(id, now + durationMillis);
		return true;
	}

	/**
	 * Rimuove le voci scadute. Da chiamare periodicamente.
	 */
	public void purgeExpired() {
		final long now = System.currentTimeMillis();
		this.expiry.values().removeIf(until -> until <= now);
	}
}