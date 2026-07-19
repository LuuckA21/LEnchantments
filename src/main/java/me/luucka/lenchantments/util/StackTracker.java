package me.luucka.lenchantments.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StackTracker {

	private static final class Entry {
		int stacks;
		long lastHitMillis;
	}

	private final Map<UUID, Entry> entries = new HashMap<>();
	private final long windowMillis;
	private final int maxStacks;

	public StackTracker(final long windowMillis, final int maxStacks) {
		this.windowMillis = windowMillis;
		this.maxStacks = maxStacks;
	}

	/**
	 * Registra un colpo e ritorna gli stack ATTIVI PRIMA di questo colpo
	 * (cioe' quelli che devono ridurre il colpo corrente).
	 * Il primo colpo di una raffica ritorna 0.
	 */
	public int hit(final Player player) {
		final long now = System.currentTimeMillis();
		final UUID id = player.getUniqueId();

		Entry entry = this.entries.computeIfAbsent(id, k -> new Entry());

		// finestra scaduta: la raffica e' finita, si riparte da zero
		if (now - entry.lastHitMillis > this.windowMillis) {
			entry.stacks = 0;
		}

		final int activeStacks = entry.stacks;   // riduzione basata su cio' che c'era PRIMA

		if (entry.stacks < this.maxStacks) {
			entry.stacks++;
		}
		entry.lastHitMillis = now;

		return activeStacks;
	}

	public void clear(final Player player) {
		this.entries.remove(player.getUniqueId());
	}
}