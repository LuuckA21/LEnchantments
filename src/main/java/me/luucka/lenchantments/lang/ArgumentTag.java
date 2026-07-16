package me.luucka.lenchantments.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ArgumentTag implements TagResolver {

	private static final String NAME = "argument";
	private static final String NAME_SHORT = "arg";

	private final List<Component> arguments;

	ArgumentTag(final @NotNull List<? extends ComponentLike> arguments) {
		this.arguments = arguments.stream()
				.map(ComponentLike::asComponent)
				.toList();
	}

	@Override
	public Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue queue, final @NotNull Context ctx) throws ParsingException {

		final int index = queue.popOr("You need argument index!")
				.asInt()
				.orElseThrow(() -> ctx.newException("Argument index not valid!", queue));

		if (index < 0 || index >= this.arguments.size()) {
			throw ctx.newException("Argument index out of bounds: " + index, queue);
		}

		return Tag.inserting(this.arguments.get(index));
	}

	@Override
	public boolean has(final @NotNull String name) {
		return NAME.equals(name) || NAME_SHORT.equals(name);
	}
}
