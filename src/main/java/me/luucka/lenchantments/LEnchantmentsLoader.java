package me.luucka.lenchantments;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public final class LEnchantmentsLoader implements PluginLoader {

	@Override
	public void classloader(PluginClasspathBuilder classpathBuilder) {
		MavenLibraryResolver resolver = new MavenLibraryResolver();
		resolver.addRepository(new RemoteRepository.Builder("jitpack.io", "default", "https://jitpack.io").build());
		resolver.addDependency(new Dependency(new DefaultArtifact("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.5"), null));
		classpathBuilder.addLibrary(resolver);
	}
}
