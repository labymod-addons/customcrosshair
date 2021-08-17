plugins {
    id("org.spongepowered.gradle.vanilla")
    id("net.labymod.gradle.mixin")
}

version = "1.0.0"

repositories {
    mavenLocal()
}

minecraft {
    version("1.17.1")
    runs {
        client() {
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.vanilla.LabyModTweaker", "--dev")
        }
    }
}

dependencies {
    annotationProcessor("net.labymod.labymod4:addon-annotation-processor:0.1.0-${project.property("net.labymod.addon-release-type")}")
    api("net.labymod.labymod4:v1_17:0.1.0-${project.property("net.labymod.addon-release-type")}")
}

mixin {
    version("1.17.1")
    addReferenceMap(sourceSets.findByName("main"), "labymod.refmap.json")
}
