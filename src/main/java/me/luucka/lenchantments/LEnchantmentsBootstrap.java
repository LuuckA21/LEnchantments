package me.luucka.lenchantments;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.event.RegistryEvents;
import me.luucka.lenchantments.registry.registration.LEnchantmentRegistration;

public class LEnchantmentsBootstrap implements PluginBootstrap {

	@Override
	public void bootstrap(BootstrapContext context) {
		context.getLifecycleManager().registerEventHandler(
				RegistryEvents.ENCHANTMENT.compose().newHandler(LEnchantmentRegistration::registerAll)
		);
	}
}
