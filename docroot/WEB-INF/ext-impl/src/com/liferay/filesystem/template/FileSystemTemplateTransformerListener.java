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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.templateparser.BaseTransformerListener;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.util.RegexTransformerUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Allen Ziegenfus
 */
public class FileSystemTemplateTransformerListener extends BaseTransformerListener {

	@Override
	public String onOutput(
		String output, String languageId, Map<String, String> tokens) {

		if (_log.isDebugEnabled()) {
			_log.debug("onOutput");
		}

		
		return output;
	}

	@Override
	public String onScript(
		String script, String xml, String languageId,
		Map<String, String> tokens) {

		if (_log.isDebugEnabled()) {
			_log.debug("onScript");
		}
		
		try {
			
			String templateKey = tokens.get("template_id");
			String groupId = tokens.get("article_group_id");
			String articleId = tokens.get("reserved_article_id");
			JournalArticle journalArticle = JournalArticleLocalServiceUtil.fetchArticle(GetterUtil.getLong(groupId), articleId);
			
			String templateId = journalArticle.getTemplateId();
					
			String directory = TemplateTransformerListenerProps.get("template.directory");
			String templateFileName = TemplateTransformerListenerProps.get(templateKey);
					
			String s = FileUtil.read(directory + templateFileName);
			
			if (s != null) {
				return s;
			} 
			else {
				
				_log.info("Could not find template for template key: " + templateId + " " + journalArticle.getTitleCurrentValue());
			}
		} catch (Exception e) {
			_log.error(e);
		}

		return replace(script);
	}

	protected String replace(String s) {
		if (s == null) {
			return s;
		}

	
			
		return s;
	}

	private static Log _log = LogFactoryUtil.getLog(
		FileSystemTemplateTransformerListener.class);

}