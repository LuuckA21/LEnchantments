package me.luucka.lenchantments.setting;

import lombok.Getter;
import me.luucka.lcore.config.SimpleConfig;
import me.luucka.lenchantments.LEnchantments;
import org.simpleyaml.configuration.implementation.api.QuoteStyle;

public final class Config extends SimpleConfig {

	private static Config instance;

	private Config(LEnchantments plugin) {
		super(plugin, "config.yml");
	}

	public static void init(LEnchantments plugin) {
		instance = SimpleConfig.create(new Config(plugin));
	}

	public static Config get() {
		if (instance == null) {
			throw new IllegalStateException("Config not initialized yet");
		}
		return instance;
	}

	public static void unload() {
		instance = null;
	}

	@Override
	protected void header() {
		file.options().headerFormatter()
				.prefixFirst("######################")
				.commentPrefix("##  ")
				.commentSuffix("  ##")
				.suffixLast("######################");


		file.setHeader("HEADER COMMENT\nnew line");
	}

	@Override
	protected void footer() {
	}

	@Override
	protected void loadValues() {
		this.prefix = file.getString("prefix");
		this.debug = file.getBoolean("debug");
	}

	@Override
	protected void loadDefaults() {
		file.path("prefix")
				.comment("Prefix for the plugin messages")
				.set("<gray>[<gradient:#C77DFF:#48CAE4><bold>✦ LEnchantments</bold></gradient><gray>] <reset>", QuoteStyle.DOUBLE);

		file.path("debug")
				.comment("Enables debug messages")
				.set(false);
	}

	//------------------------------------------------------------------------------------------------------------------

	@Getter
	private String prefix;

	@Getter
	private boolean debug;

}
