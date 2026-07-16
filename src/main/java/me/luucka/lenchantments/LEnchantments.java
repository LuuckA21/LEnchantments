package me.luucka.lenchantments;

import me.luucka.lenchantments.effect.DamageModifyingEffect;
import me.luucka.lenchantments.effect.PostDamageEffect;
import me.luucka.lenchantments.effect.damagemodifying.ExecutionEffect;
import me.luucka.lenchantments.effect.postdamage.VampirismEffect;
import me.luucka.lenchantments.lang.LanguageManager;
import me.luucka.lenchantments.listener.DamageModifyingEffectListener;
import me.luucka.lenchantments.listener.PostDamageEffectListener;
import me.luucka.lenchantments.registry.LEnchantmentList;
import me.luucka.lenchantments.registry.registration.LEnchantmentRegistration;
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
		final List<DamageModifyingEffect> damageModifyingEffects = List.of(
				new ExecutionEffect(LEnchantmentRegistration.EXECUTION.enchantment())
		);

		final List<PostDamageEffect> postDamageEffects = List.of(
				new VampirismEffect(LEnchantmentRegistration.VAMPIRISM.enchantment())
		);

		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new DamageModifyingEffectListener(damageModifyingEffects), this);
		pm.registerEvents(new PostDamageEffectListener(postDamageEffects), this);
	}
}