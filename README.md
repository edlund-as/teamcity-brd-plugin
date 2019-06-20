# Build result decorator Teamcity plugin.


The plugin can add custom HTML to the build result overview. The HTML is retrieved from a artifact file that can either be a artifact filename or
a file within a zip archive. The artifact must be encoded using UTF-8.
 
## Build
Issue 'mvn package' command from the root project to build your plugin. The resulting package 'brdtcplugin.zip' will be placed in 'target' directory. 

## Install
To install the plugin, upload the zip archive from the Administration->Plugins List page.
