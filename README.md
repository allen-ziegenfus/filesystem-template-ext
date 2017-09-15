# filesystem-template-ext

This is a simple ext project to help speed up local template development in Liferay. 

Instead of having to update templates in the Liferay UI this ext modifies code so that the portal will directly look at the fileystem instead. This way as soon as you save any template changes in a text editor of your choice, the portal will reflect those changes. 

# How to set up

To get this working we have map templateKeys to files so that the code knows where to look. First configure this properties file in the ext with the path of your custom configuration file:

https://github.com/allen-ziegenfus/filesystem-template-ext/blob/master/docroot/WEB-INF/ext-impl/src/template.properties

Then create the properties file and add some entries. There is one entry for the template directory path and the rest are for individual template files


#. template.directory - this is the base path of where the code will look for files
#. <templateKey> - pathname of a particular template file, e.g.: 

218983270=/6.2.x/templates/events/event_sessions.ftl


