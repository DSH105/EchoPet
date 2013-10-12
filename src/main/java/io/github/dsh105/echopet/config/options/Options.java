package io.github.dsh105.echopet.config.options;

import io.github.dsh105.echopet.config.YAMLConfig;

/**
 * Project by DSH105
 */

public abstract class Options {

	protected YAMLConfig config;

	public Options(YAMLConfig config) {
		this.config = config;
	}

	public abstract void setDefaults();

	protected void set(String path, Object defObject, String... comments) {
		this.config.set(path, this.config.get(path, defObject), comments);
	}
}