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
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.template.DDMTemplateResourceParser;
import com.liferay.portal.template.TemplateResourceParser;

/**
 * @author Allen Ziegenfus
 */
public class FileSystemTemplateResourceParser extends DDMTemplateResourceParser
	implements TemplateResourceParser {

	public TemplateResource getTemplateResource(String templateId)
		throws TemplateException {

		TemplateResource templateResource = super.getTemplateResource(
			templateId);

		if (templateResource == null) {
			return null;
		}
		else {
			try {
				String directory = TemplateTransformerListenerProps.get(
					"template.directory");

				String templateFileName =
					TemplateTransformerListenerProps.get(
						templateResource.getTemplateId());

				String s = FileUtil.read(directory + templateFileName);

				if (s != null) {
					if (_log.isInfoEnabled()) {
						_log.info(
							"Serving templateKey: " +
							templateResource.getTemplateId() + " from " +
								directory + templateFileName
						);
					}

					return new StringTemplateResource(
						templateResource.getTemplateId(), s);
				}
				else {
					if (_log.isInfoEnabled()) {
						_log.info(
							"Could not find template for template key: " +
							templateResource.getTemplateId()
						);
					}
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		return templateResource;
	}

	private static Log _log = LogFactoryUtil.getLog(
		FileSystemTemplateResourceParser.class);

}