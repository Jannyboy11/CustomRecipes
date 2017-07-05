# CustomRecipes

CustomRecipes is a plugin (serverside mod) for Minecraft that works on [Spigot](https://www.spigotmc.org/).
As the name suggests, the plugin allows you to create custom recipes.

Link to the [SpigotMC page](https://www.spigotmc.org/resources/custom-recipes.11440/) of the plugin.

# Features

- Easy commands for creating and deleting custom recipes
- Inventory menu for viewing all registered recipes
- Supports crafting and furnace recipes
- Recipes are saved in the NBT format, preserving all item data
- Extra types of recipes: Permission Recipe and NBT-specific Recipe
- Developers API, providing more access than the Bukkit API.
- Spigot 1.12 compatible

# Compiling

I'm using Apache Maven, however there is one dependency that is not available in a public repository, that is the Spigot server jar.
To get Spigot, download [BuildTools](https://www.spigotmc.org/wiki/buildtools/) (make sure to read that wiki page) and run it with ```java -jar BuildTools.jar --rev 1.12``` in your git shell.
This should compile the 1.12 build of Spigot for you.

If you own a private maven repository, you can put it up there and add it to the pom.xml of this project, but if you don't you can install it in your local repository.
Run ```mvn install:install-file -Dfile=spigot-1.12.jar -DgroupId=org.spigotmc -DartifactId=Spigot -Dversion=1.12-R0.1-SNAPSHOT -Dpackaging=jar```.
More on this [here](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html).
Once you've done this, the dependencies in the pom.xml should resolve.

Then run ```mvn package clean install``` and your server-ready plugin jar is ready in your folder. Be sure to use the shaded one.

# FAQ

Q: I used your plugin with previous versions of minecraft, but with the 1.12 update none of the recipes work anymore.

A: Run the command ```/migraterecpes``` to migrate the custom recipes to the new format. This may temporarily lag your server if you have many recipes installed.



