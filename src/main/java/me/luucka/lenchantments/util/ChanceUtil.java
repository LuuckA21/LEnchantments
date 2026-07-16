package me.luucka.lenchantments.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChanceUtil {

	/**
	 * @param chance probabilità di successo (0..1)
	 */
	public static boolean succeeds(final double chance) {
		if (!(chance > 0.0)) {
			return false;
		}

		if (chance >= 1.0) {
			return true;
		}

		return ThreadLocalRandom.current().nextDouble() < chance;
	}
}
