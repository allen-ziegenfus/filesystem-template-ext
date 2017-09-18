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
import com.liferay.portal.kernel.templateparser.BaseTransformerListener;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

import java.util.Map;

/**
 * @author Allen Ziegenfus
 */
public class FileSystemTemplateTransformerListener
	extends BaseTransformerListener {

	@Override
	public String onScript(
		String script, String xml, String languageId,
		Map<String, String> tokens) {

		try {
			String articleId = tokens.get("reserved_article_id");
			String groupId = tokens.get("article_group_id");
			String templateKey = tokens.get("template_id");

			String directory = TemplateTransformerListenerProps.get(
				"template.directory");
			String templateFileName = TemplateTransformerListenerProps.get(
				templateKey);

			String s = FileUtil.read(directory + templateFileName);

			if (s != null) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Serving templateKey: " + templateKey + " from " +
							directory + templateFileName
					);
				}

				return s;
			}
			else {
				JournalArticle journalArticle =
					JournalArticleLocalServiceUtil.fetchArticle(
						GetterUtil.getLong(groupId), articleId);

				String templateId = journalArticle.getTemplateId();

				if (_log.isInfoEnabled()) {
					_log.info(
						"Could not find template for template key: " +
						templateId + " Article: " +
						journalArticle.getTitleCurrentValue()
					);
				}
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		return script;
	}

	private static Log _log = LogFactoryUtil.getLog(
		FileSystemTemplateTransformerListener.class);

}