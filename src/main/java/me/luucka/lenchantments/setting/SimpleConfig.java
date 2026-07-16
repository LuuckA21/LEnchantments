package me.luucka.lenchantments.setting;

import me.luucka.lenchantments.LEnchantments;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;

public abstract class SimpleConfig {

	protected final YamlFile file;
	private final String fileName;

	public SimpleConfig(final LEnchantments plugin, final String fileName) {
		this.fileName = fileName;
		this.file = new YamlFile(new File(plugin.getDataFolder(), fileName));
	}

	protected static <T extends SimpleConfig> T create(final T instance) {
		instance.initialize();
		return instance;
	}

	void initialize() {
		try {
			/*
			 * Se il file non esiste:
			 * - crea il file;
			 * - crea anche le cartelle mancanti.
			 *
			 * Se esiste:
			 * - carica valori e commenti.
			 */
			file.createOrLoadWithComments();
			file.setCommentFormat(YamlCommentFormat.PRETTY);

			header();

			loadDefaults();

			footer();

			/*
			 * Copia nel file i default mancanti senza
			 * sovrascrivere i valori modificati dall'utente.
			 */
			file.options().copyDefaults(true);
			file.save();

			loadValues();
		} catch (IOException exception) {
			throw new IllegalStateException("Unable to initialize " + fileName, exception);
		}
	}

	protected abstract void header();

	protected abstract void footer();

	protected abstract void loadValues();

	protected abstract void loadDefaults();

	public void reload() {
		try {
			/*
			 * Rilegge realmente il contenuto presente su disco.
			 */
			file.loadWithComments();

			/*
			 * Garantisce che eventuali nuove impostazioni
			 * aggiunte nelle versioni future vengano inserite.
			 */
			loadDefaults();
			file.options().copyDefaults(true);
			file.save();

			/*
			 * Aggiorna i campi Java con i nuovi valori.
			 */
			loadValues();
		} catch (IOException exception) {
			throw new IllegalStateException("Unable to reload " + fileName, exception);
		}
	}

	public void save() {
		try {
			file.save();
		} catch (IOException exception) {
			throw new IllegalStateException("Unable to save " + fileName, exception);
		}
	}

}