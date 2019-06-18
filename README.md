# Build result decorator Teamcity plugin.


The plugin can add custom HTML to the build result overview. The HTML is retrieved from a artifact file that can either be a artifact filename or
a file within a zip archive. The artifact must be encoded using UTF-8.
 
## Build
Issue 'mvn package' command from the root project to build your plugin. Resulting package <artifactId>.zip will be placed in 'target' directory. 

## Install
To install the plugin, put zip archive to 'plugins' dir under TeamCity data directory and restart the server.
