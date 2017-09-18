/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.filesystem.template;

import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.FileReader;

import java.util.Properties;

/**
 * @author Allen Ziegenfus
 */
public class TemplateTransformerListenerProps {

	public static String get(String key) {
		return loadProps().getProperty(key);
	}

	private static String getPropertiesFileName() {
		Configuration configuration = ConfigurationFactoryUtil.getConfiguration(
			TemplateTransformerListenerProps.class.getClassLoader(),
			"template");

		return configuration.get("template.properties.file");
	}

	private static Properties loadProps() {
		Properties properties = new Properties();

		try {
			FileReader reader = new FileReader(getPropertiesFileName());

			properties.load(reader);

			reader.close();
		}
		catch (Exception e) {
			_log.error(e);
		}

		return properties;
	}

	private static Log _log = LogFactoryUtil.getLog(
		TemplateTransformerListenerProps.class);

}