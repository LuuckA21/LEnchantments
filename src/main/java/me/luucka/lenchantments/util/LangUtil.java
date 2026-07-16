package me.luucka.lenchantments.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LangUtil {

	public static Component msg(final String key, final Object... args) {
		final Component[] components = Arrays.stream(args)
				.map(LangUtil::toComponent)
				.toArray(Component[]::new);
		return Component.translatable(key, components);
	}

	private static Component toComponent(final Object o) {
		if (o instanceof ComponentLike cl) return cl.asComponent();
		return Component.text(String.valueOf(o));
	}
}
