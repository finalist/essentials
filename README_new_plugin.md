The existing Hippo essentials plugins are located in dashboard-plugins.

## Types of plugins:
There are different types of plugins, depending on the fields specified in the plugin_descriptor.json.
(For more information about these fields check http://www.onehippo.org/library/essentials/plugin/development/plugin-descriptor.html)

1. packageFile field specified  – these are plugins which execute a set of instructions.
Ex:
- selectionPlugin
- seoPlugin
- bannerPlugin
- robotsPlugin
- pollPlugin
- searchPlugin
- imageComponentPlugin
- eventsPlugin
- videoComponentPlugin
- faqPlugin
- menuPlugin
- simpleContent
- listContentPlugin
- newsPlugin

2. restClasses field specified - plugins which do more than just execute a set of instructions. If restClasses are specified, packageFile will be ignored
Ex:
- taxonomyPlugin
- ecmTaggingPlugin
- documentWizardPlugin
- relatedDocumentPlugin
- beanWriter
- cloneComponent
- freemarkerSync
- restServices

3. no PackageFile en no restClasses specified
Ex:
- contentBlocks
- galleryPlugin


## Creating a Hippo Essentials Plugin:

In order to quickly create an Hippo Essentials plugin with restClasses, an Intellij Idea plugin can be used.
For the installation of this IDE plugin, please follow the steps bellow:

- On a Mac OS, Intellij Idea, navigate to Preferences.  From there select Plugins and click on “Install plugin from disk…”.  Find the jar ide-integration.jar and select it.)
- Restart Intellij and use: File > New project > Hippo Essentials plugin
- If this doesn't work or you get errors in the Intellij Idea logs, jdk6 is probably used, which can be changed like this:

To force running under JDK 1.7 edit /Applications/<Product>.app/Contents/Info.plist file, change JVMVersion from 1.6* to 1.7* :

## Using a new Hippo Essentials Plugin:

The new module should be copied in the dashboard-plugins.
The following files should be modified to include the new plugin:
- plugin_descriptor.json from plugin-dashboard
- pom.xml from plugin-dashboard
- pom.xml from dashboard-plugins

###Build essentials project:
```
$ cd essentials
$ mvn clean install
```

###Build and run your hippo project (myhippoproject)
```
$ cd myhippoproject
$ mvn clean install
$ mvn -P cargo.run
```
