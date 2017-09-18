# filesystem-template-ext

This is a simple ext project to help speed up local template development in Liferay. This currently works with DDMTemplates. 

Instead of having to update templates in the Liferay UI this ext modifies code so that the portal will directly look at the fileystem instead. This way as soon as you save any template changes in a text editor of your choice, the portal will reflect those changes. 

# How to set up

## Config properties configuration file 
To get this working we have map templateKeys to files so that the code knows where to look. First configure this properties file in the ext with the path of your custom configuration file:

https://github.com/allen-ziegenfus/filesystem-template-ext/blob/master/docroot/WEB-INF/ext-impl/src/template.properties

## Configure template properties file
Then create the properties file and add some entries. There is one entry for the template directory path and the rest are for individual template files

template.directory - this is the base path of where the code will look for files

(templateKey) - pathname of a particular template file, e.g.: 

```
	218983270=/6.2.x/templates/events/event_sessions.ftl
```

Here is a sample tempalte config file: https://github.com/allen-ziegenfus/filesystem-template-ext/blob/master/template.properties

To get the appropriate templateKeys you can either look through the portal / db or you can also set some logging which will spit out a message if this ext *DOES NOT* find a match: 

	<logger name="com.liferay.filesystem.template">
		<level value="DEBUG" />
	</logger>

Then you'll get messages in your log file like this: 

### For a Journal Article:

```
12:42:09,148 INFO  [RuntimePageImpl-14][FileSystemTemplateTransformerListener:75] Could not find template for template key: 203515550 Article: Countdown Timer LDSF
```

So here you can add a new entry for 18611

### For an imported template
```
12:37:16,622 INFO  [RuntimePageImpl-5][FileSystemTemplateResourceParser:62] Could not find template for template key: 898140
```
Here you would add the missing entry for 898140

## Portal properties 
Finally change these portal properties to get the ext working: 

freemarker.engine.template.parsers - replace com.liferay.portal.template.DDMTemplateResourceParser with  com.liferay.filesystem.template.FileSystemTemplateResourceParser

journal.transformer.listener - add com.liferay.filesystem.template.FileSystemTemplateTransformerListener as the first transformer

```
freemarker.engine.template.parsers=com.liferay.portal.freemarker.FreeMarkerServletResourceParser,com.liferay.portal.template.ThemeResourceParser,com.liferay.filesystem.template.FileSystemTemplateResourceParser,com.liferay.portal.template.ClassLoaderResourceParser


journal.transformer.listener=com.liferay.filesystem.template.FileSystemTemplateTransformerListener,com.liferay.portlet.journal.util.TokensTransformerListener,com.liferay.portlet.journal.util.ContentTransformerListener,com.liferay.portlet.journal.util.LocaleTransformerListener,com.liferay.portlet.journal.util.RegexTransformerListener,com.liferay.portlet.journal.util.ViewCounterTransformerListener
```




