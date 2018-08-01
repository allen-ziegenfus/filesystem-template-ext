# filesystem-template-ext

This is a simple ext project to help speed up local template development in Liferay. This currently works with DDMTemplates. 

Instead of having to update templates in the Liferay UI this ext modifies code so that the portal will directly look at the fileystem instead. This way as soon as you save any template changes in a text editor of your choice, the portal will reflect those changes. In combination with a git repository this means you can easily switch your portal between different branches of templates. Yay!

# How to set up

To get this working we have to map templateKeys to files so that the ext knows where to look for the code. There are two configuration files: one built with the ext that has the location of the template configuration file and the template configuration file. 

## Config properties configuration file 
 First configure this properties file in the ext with the path of your custom configuration file:

https://github.com/allen-ziegenfus/filesystem-template-ext/blob/master/docroot/WEB-INF/ext-impl/src/template.properties

## Configure template properties file
Then create the template properties file and add some entries. There is one entry for the template directory path and the rest are for the individual template files, as follows:

template.directory - this is the base path of where the code will look for files

(templateKey) - pathname of a particular template file, e.g.: 

```
218983270=/6.2.x/templates/events/event_sessions.ftl
```

or, for classloader loaded templates: 

```
osb_www_components.ftl=/osb_www_components.ftl
```

Here is a sample template config file: https://github.com/allen-ziegenfus/filesystem-template-ext/blob/master/template.properties

To get the appropriate templateKeys you can either look through the portal / db or you can also set some logging which will spit out a message if this ext *DOES NOT* find a match: 

	<logger name="com.liferay.filesystem.template">
		<level value="DEBUG" />
	</logger>

Then you'll get messages in your log file like this: 

### For a Journal Article:

```
12:42:09,148 INFO  [RuntimePageImpl-14][FileSystemTemplateTransformerListener:75] Could not find template for template key: 203515550 Article: Countdown Timer LDSF
```

So here you can add a new entry for 203515550

### For an imported template
```
12:37:16,622 INFO  [RuntimePageImpl-5][FileSystemTemplateResourceParser:62] Could not find template for template key: 898140
```
Here you would add the missing entry for 898140

## Build and deploy your ext
```
ant direct-deploy
```
## Portal properties 
Finally change these portal properties to get the ext working: 

freemarker.engine.template.parsers - replace com.liferay.portal.template.DDMTemplateResourceParser with  com.liferay.filesystem.template.FileSystemTemplateResourceParser (for templates loaded through the DDMTemplate service) and  com.liferay.portal.template.ClassLoaderResourceParser with 
com.liferay.filesystem.template.FileSystemClassLoaderResourceParser (for templates loaded from the class path)

journal.transformer.listener - add com.liferay.filesystem.template.FileSystemTemplateTransformerListener as the first transformer

```
freemarker.engine.template.parsers=com.liferay.portal.freemarker.FreeMarkerServletResourceParser,com.liferay.portal.template.ThemeResourceParser,com.liferay.filesystem.template.FileSystemTemplateResourceParser,com.liferay.filesystem.template.FileSystemClassLoaderResourceParser


journal.transformer.listener=com.liferay.filesystem.template.FileSystemTemplateTransformerListener,com.liferay.portlet.journal.util.TokensTransformerListener,com.liferay.portlet.journal.util.ContentTransformerListener,com.liferay.portlet.journal.util.LocaleTransformerListener,com.liferay.portlet.journal.util.RegexTransformerListener,com.liferay.portlet.journal.util.ViewCounterTransformerListener
```

# Running in Docker

To use an ext plugin in Liferay, you first have to deploy and then restart Liferay (e.g. tomcat). Because this restart is necessary, in a Docker context, it is critical to use a volume for the tomcat directory, so that it retains state. A volume can be defined in docker-composer.yaml by adding a line in the service definition and to the list of volumes as follows

## Volume Setting in the service definition 
```
      - "62_liferay_tomcat:/opt/java/liferay/tomcat"
```

## List of volumes

```
volumes:
  62_liferay_tomcat:
```

To actually get Liferay to look on the host filesystem for templates for a virtualized Docker installation (Mac / Windows), a bind mount is necessary. Assuming that my templates are on my host filesystem in the directory ~/liferay/web-dev-lrdcom, I would add the following line to the service definition: 

```
    - "~/liferay/web-dev-lrdcom:/opt/java/liferay/web-dev-lrdcom"
```

This defines the folder /opt/java/liferay/web-dev-lrdcom inside the docker image to point to my host template directory. 

Then set up the ```template.properties.file``` and ```template.directory``` to point to /opt/java/liferay/web-dev-lrdcom. 

## template.properties inside the ext plugin

```
template.properties.file=/opt/java/liferay/web-dev-lrdcom/template.properties
```

## template.properties on the host filesystem

```
template.directory=/opt/java/liferay/web-dev-lrdcom/
231473786=/6.2.x/templates/events/event_sessions_recap.ftl
osb_www_components.ftl=/osb_www_components.ftl
```

The line starting with ```231473786``` defines the location for a DDMTemplate with they key 231473786.

The line starting with ```osb_www_components.ftl``` maps the location for a template loaded via the ClassLoader, for example for adding to the global freemarker library. 

