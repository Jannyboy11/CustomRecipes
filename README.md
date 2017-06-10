# CustomRecipes

CustomRecipes is a plugin (serverside mod) for Minecraft that works on Spigot.
As the name suggests, the plugin allows you to create custom recipes.

For the 1.12 update I decided to rewrite the plugin from scratch because the old code wasn't all that pretty, and the Minecraft crafting internals have changed quite a bit.
The code is still in early stage of development.

Older versions of the plugin can be found [here](https://www.spigotmc.org/resources/custom-recipes.11440/)

# Planned Features

- Create and delete custom crafting recipes and furnace recipes
- Custom crafting recipes implementations such as NBT-specific recipes and permission recipes.
- Developers API

# Compiling

I'm using Apache Maven, however there is one dependency that is not available in a public repository, that is the Spigot server jar.
To get Spigot, download [BuildTools](https://www.spigotmc.org/wiki/buildtools/) (make sure to read that wiki page) and run it with ```java -jar BuildTools.jar --rev 1.12``` in your git shell.
This should compile the 1.12 build of Spigot for you.


If you own a private maven repository, you can put it up there and add it to the pom.xml of this project, but if you don't you can install it in your local repository.
Run ```mvn install:install-file -Dfile=spigot-1.12.jar -DgroupId=org.spigotmc -DartifactId=Spigot -Dversion=1.12-R0.1-SNAPSHOT -Dpackaging=jar```.
More on this [here](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html).
Once you've done this, the dependencies in the pom.xml should resolve.

Then run ```mvn package clean install``` and your server-ready plugin jar is ready in your folder. Be sure to use the shaded one.