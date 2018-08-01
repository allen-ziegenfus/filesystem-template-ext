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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.template.URLResourceParser;
import com.liferay.portal.template.TemplateResourceParser;
import com.liferay.portal.kernel.util.FileUtil;

import java.net.URL;

/**
 * @author Allen Ziegenfus
 */
public class FileSystemClassLoaderResourceParser extends URLResourceParser
	implements TemplateResourceParser {

@Override
	@SuppressWarnings("deprecation")
	public URL getURL(String templateId) {
		if (templateId.contains(TemplateConstants.JOURNAL_SEPARATOR) ||
			templateId.contains(TemplateConstants.SERVLET_SEPARATOR) ||
			templateId.contains(TemplateConstants.TEMPLATE_SEPARATOR) ||
			templateId.contains(TemplateConstants.THEME_LOADER_SEPARATOR)) {

			return null;
		}

		try {
			String directory = TemplateTransformerListenerProps.get(
				"template.directory");

			String templateFileName =
				TemplateTransformerListenerProps.get(templateId);

			String s = FileUtil.read(directory + templateFileName);

			if (s != null) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Serving templateKey: " + templateId + " from " +
							directory + templateFileName
					);
				}

				String urlAsString = "file:///" + directory + templateFileName; 

				URL url = new URL(urlAsString);

				return url;
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		if (_log.isDebugEnabled()) {
			_log.debug("Loading " + templateId);
		}

		return classLoader.getResource(templateId);
	}

	private static Log _log = LogFactoryUtil.getLog(
		FileSystemClassLoaderResourceParser.class);

}