package com.craftingdead.mod.common.core;

import java.io.File;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

/**
 * All normal mod behaviours are available through this class - this container
 * will receive and handle normal loading events
 * 
 * @author Sm0keySa1m0n
 *
 */
public class CDModContainer implements ModContainer {

	private int classVersion;

	@Override
	public String getModId() {
		return CraftingDead.MOD_ID;
	}

	@Override
	public String getName() {
		return CraftingDead.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return CraftingDead.MOD_VERSION;
	}

	@Override
	public File getSource() {
		return CraftingDead.instance().getModLocation();
	}

	@Override
	public ModMetadata getMetadata() {
		ModMetadata metadata = new ModMetadata();
		metadata.modId = getModId();
		metadata.name = getName();
		metadata.version = getVersion();
		return metadata;
	}

	@Override
	public void bindMetadata(MetadataCollection mc) {
		;
	}

	@Override
	public void setEnabledState(boolean enabled) {
		;
	}

	@Override
	public Set<ArtifactVersion> getRequirements() {
		return Collections.emptySet();
	}

	@Override
	public List<ArtifactVersion> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public List<ArtifactVersion> getDependants() {
		return Collections.emptyList();
	}

	@Override
	public String getSortingRules() {
		return new String();
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return CraftingDead.instance().registerBus(bus, controller);
	}

	@Override
	public boolean matches(Object mod) {
		return mod instanceof CraftingDead;
	}

	@Override
	public Object getMod() {
		return CraftingDead.instance();
	}

	@Override
	public ArtifactVersion getProcessedVersion() {
		return new DefaultArtifactVersion(getModId(), getVersion());
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public String getDisplayVersion() {
		return getVersion();
	}

	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		return VersionParser.parseRange("1.12.2");
	}

	@Override
	public Certificate getSigningCertificate() {
		return null;
	}

	@Override
	public Map<String, String> getCustomModProperties() {
		return EMPTY_PROPERTIES;
	}

	@Override
	public Class<?> getCustomResourcePackClass() {
		return CDResourcePack.class;
	}

	@Override
	public Map<String, String> getSharedModDescriptor() {
		return null;
	}

	@Override
	public Disableable canBeDisabled() {
		return Disableable.NEVER;
	}

	@Override
	public String getGuiClassName() {
		return null;
	}

	@Override
	public List<String> getOwnedPackages() {
		return ImmutableList.of("com.craftingdead.mod.client", "com.craftingdead.mod.server",
				"com.craftingdead.mod.common.server");
	}

	@Override
	public boolean shouldLoadInEnvironment() {
		return false;
	}

	@Override
	public URL getUpdateUrl() {
		return null;
	}

	@Override
	public void setClassVersion(int classVersion) {
		this.classVersion = classVersion;
	}

	@Override
	public int getClassVersion() {
		return classVersion;
	}

}
