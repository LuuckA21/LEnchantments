package me.luucka.lenchantments;

import me.luucka.lenchantments.effect.DamageDealtModifier;
import me.luucka.lenchantments.effect.DamageDealtReaction;
import me.luucka.lenchantments.effect.DamageTakenReaction;
import me.luucka.lenchantments.effect.dealt.ExecutionEffect;
import me.luucka.lenchantments.effect.dealt.VampirismEffect;
import me.luucka.lenchantments.effect.taken.SecondWindEffect;
import me.luucka.lenchantments.lang.LanguageManager;
import me.luucka.lenchantments.listener.DamageDealtListener;
import me.luucka.lenchantments.listener.DamageTakenListener;
import me.luucka.lenchantments.registry.LEnchantmentList;
import me.luucka.lenchantments.registry.registration.LEnchantmentRegistration;
import me.luucka.lenchantments.util.CooldownStore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public final class LEnchantments extends JavaPlugin {

	private static LEnchantments instance;

	private LanguageManager languageManager;

	@Override
	public void onLoad() {
		try {
			getInstance();
		} catch (final Throwable throwable) {
			this.setEnabled(false);
		}
	}

	@Override
	public void onEnable() {
//		this.languageManager = new LanguageManager(this);
//		languageManager.load();
//		GlobalTranslator.translator().addSource(languageManager);

		LEnchantmentList.initialize();
		registerEnchantmentListeners();

//		Config.init(this);
	}

	@Override
	public void onDisable() {
		Objects.requireNonNull(instance, "Instance of " + this.getDataFolder().getName() + " already nulled!");
		instance = null;
//		if (this.languageManager != null) {
//			GlobalTranslator.translator().removeSource(this.languageManager);
//		}
	}

	public static LEnchantments getInstance() {
		if (instance == null) {
			try {
				instance = JavaPlugin.getPlugin(LEnchantments.class);

			} catch (final IllegalStateException ex) {
				if (Bukkit.getPluginManager().getPlugin("PlugMan") != null || Bukkit.getPluginManager().getPlugin("PlugManX") != null)
					Bukkit.getLogger().severe("Failed to get instance of the plugin, if you reloaded using PlugMan you need to do a clean restart instead.");

				throw ex;
			}

			Objects.requireNonNull(instance, "Cannot get a new instance! Have you reloaded?");
		}

		return instance;
	}

	public void reloadLanguages() {
//		this.languageManager.load();
	}

	private void registerEnchantmentListeners() {
		final CooldownStore cooldowns = new CooldownStore();

		final List<DamageDealtModifier> dealtModifiers = List.of(
				new ExecutionEffect(LEnchantmentRegistration.EXECUTION.enchantment())
		);

		final List<DamageDealtReaction> dealtReactions = List.of(
				new VampirismEffect(LEnchantmentRegistration.VAMPIRISM.enchantment())
		);

		final List<DamageTakenReaction> takenReactions = List.of(
				new SecondWindEffect(LEnchantmentRegistration.SECOND_WIND.enchantment(), cooldowns)
		);

		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new DamageDealtListener(dealtModifiers, dealtReactions), this);
		pm.registerEvents(new DamageTakenListener(this, takenReactions), this);

		// purga le voci scadute ogni 5 minuti
		getServer().getScheduler().runTaskTimer(this, cooldowns::purgeExpired, 6000L, 6000L);
	}
}