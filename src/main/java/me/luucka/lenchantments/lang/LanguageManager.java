package me.luucka.lenchantments.lang;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.Translator;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LanguageManager extends MiniMessageTranslator {

	private static final List<String> BUNDLED = List.of("en_us", "it_it");
	private static final Locale FALLBACK = Locale.US;

	private final Key name;
	private final JavaPlugin plugin;
	private final Map<Locale, Map<String, String>> translations = new HashMap<>();

	public LanguageManager(final @NotNull JavaPlugin plugin) {
		this.plugin = plugin;
		this.name = Key.key(new NamespacedKey(plugin, "lenchantments"), "lang");
	}

	@Override
	public @NotNull Key name() {
		return this.name;
	}

	public void load() {
		this.translations.clear();

		final File langDir = new File(this.plugin.getDataFolder(), "lang");

		// 1. estrae i file bundled se mancanti (non sovrascrive le modifiche dell'utente)
		for (final String code : BUNDLED) {
			final String path = "lang/" + code + ".yml";
			if (!new File(langDir, code + ".yml").exists()) {
				this.plugin.saveResource(path, false);
			}
		}

		// 2. carica TUTTO ciò che c'è nella cartella (anche lingue aggiunte dall'utente)
		final File[] files = langDir.listFiles((dir, n) -> n.endsWith(".yml"));
		if (files == null) return;

		for (final File file : files) {
			final String code = file.getName().substring(0, file.getName().length() - 4);
			final Locale locale = Translator.parseLocale(code);

			if (locale == null) {
				this.plugin.getLogger().warning("Language file name not valid: " + file.getName());
				continue;
			}

			final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
			final Map<String, String> map = new HashMap<>();

			for (final String key : yaml.getKeys(true)) {
				if (yaml.isString(key)) {
					map.put(key, yaml.getString(key));
				}
			}

			this.translations.put(locale, map);
			this.plugin.getLogger().info("Loaded language " + locale + " (" + map.size() + " keys)");
		}
	}

	@Override
	protected @Nullable String getMiniMessageString(final @NotNull String key, final @NotNull Locale locale) {
		// 1. match esatto (it_IT)
		final Map<String, String> exact = this.translations.get(locale);
		if (exact != null) {
			final String v = exact.get(key);
			if (v != null) return v;
		}

		// 2. stessa lingua, paese diverso (it_CH -> it_IT)
		for (final var entry : this.translations.entrySet()) {
			if (entry.getKey().getLanguage().equals(locale.getLanguage())) {
				final String v = entry.getValue().get(key);
				if (v != null) return v;
			}
		}

		// 3. fallback
		final Map<String, String> fb = this.translations.get(FALLBACK);
		return fb == null ? null : fb.get(key);
	}
}