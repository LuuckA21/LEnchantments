package me.luucka.lenchantments.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.Translator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class MiniMessageTranslator implements Translator {

	private final MiniMessage miniMessage;

	protected MiniMessageTranslator() {
		this(MiniMessage.miniMessage());
	}

	protected MiniMessageTranslator(final MiniMessage miniMessage) {
		this.miniMessage = miniMessage;
	}
	
	protected abstract String getMiniMessageString(final String key, final Locale locale);

	@Override
	public final MessageFormat translate(final String key, final Locale locale) {
		return null;
	}

	@Override
	public boolean canTranslate(final String key, final Locale locale) {
		return getMiniMessageString(key, locale) != null;
	}

	@Override
	public Component translate(final TranslatableComponent component, final Locale locale) {
		final String raw = getMiniMessageString(component.key(), locale);
		if (raw == null) return null;

		final Component result = this.miniMessage.deserialize(raw, new ArgumentTag(component.arguments()));

		// lo stile del TranslatableComponent originale ha priorità sulla root
		final Component styled = result.style(component.style().merge(result.style()));

		// niente figli sull'originale: non toccare quelli del deserialize
		if (component.children().isEmpty()) {
			return styled;
		}

		// altrimenti APPENDI, non sostituire
		final List<Component> children = new ArrayList<>(styled.children());
		children.addAll(component.children());
		return styled.children(children);
	}
}
