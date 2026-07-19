package me.luucka.lenchantments;

import me.luucka.lcore.LCorePlugin;
import me.luucka.lenchantments.effect.dealt.ExecutionEffect;
import me.luucka.lenchantments.effect.dealt.VampirismEffect;
import me.luucka.lenchantments.effect.taken.BulwarkEffect;
import me.luucka.lenchantments.effect.taken.SecondWindEffect;
import me.luucka.lenchantments.lang.LanguageManager;
import me.luucka.lenchantments.listener.BulwarkCleanupListener;
import me.luucka.lenchantments.listener.DamageDealtListener;
import me.luucka.lenchantments.listener.DamageTakenListener;
import me.luucka.lenchantments.registry.LEnchantmentList;
import me.luucka.lenchantments.registry.registration.LEnchantmentRegistration;
import me.luucka.lenchantments.setting.Config;
import me.luucka.lenchantments.util.CooldownStore;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public final class LEnchantments extends LCorePlugin {

	private static LEnchantments instance;

	private LanguageManager languageManager;

	@Override
	protected void onPluginStart() {
		Config.init(this);
		LEnchantmentList.initialize();
		registerEnchantmentListeners();
	}

	private void registerEnchantmentListeners() {
		final CooldownStore cooldowns = new CooldownStore();

		final BulwarkEffect bulwark = new BulwarkEffect(reg(LEnchantmentRegistration.BULWARK));

		final DamageDealtListener dealt = new DamageDealtListener(
				// DamageDealtModifier
				List.of(new ExecutionEffect(reg(LEnchantmentRegistration.EXECUTION))),   // modifier, HIGH

				// DamageDealtReaction
				List.of(new VampirismEffect(reg(LEnchantmentRegistration.VAMPIRISM)))    // reaction, MONITOR
		);

		final DamageTakenListener taken = new DamageTakenListener(this,
				// DamageTakenModifier
				List.of(bulwark),                                              // modifier, HIGH

				// DamageTakenReaction
				List.of(new SecondWindEffect(reg(LEnchantmentRegistration.SECOND_WIND), cooldowns))     // reaction, MONITOR
		);

		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(dealt, this);
		pm.registerEvents(taken, this);
		pm.registerEvents(new BulwarkCleanupListener(bulwark), this);

		getServer().getScheduler().runTaskTimer(this, cooldowns::purgeExpired, 6000L, 6000L);
	}

	private static Enchantment reg(final LEnchantmentRegistration r) {
		return r.enchantment();
	}

	@Override
	public String[] getStartupLogo() {
		return new String[]{
				" _     _____            _                 _                        _       ",
				"| |   | ____|_ __   ___| |__   __ _ _ __ | |_ _ __ ___   ___ _ __ | |_ ___ ",
				"| |   |  _| | '_ \\ / __| '_ \\ / _` | '_ \\| __| '_ ` _ \\ / _ \\ '_ \\| __/ __|",
				"| |___| |___| | | | (__| | | | (_| | | | | |_| | | | | |  __/ | | | |_\\__ \\",
				"|_____|_____|_| |_|\\___|_| |_|\\__,_|_| |_|\\__|_| |_| |_|\\___|_| |_|\\__|___/",
				"                                                                           "
		};
	}
}