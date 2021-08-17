plugins {
	id("net.labymod.gradle.legacyminecraft")
	id("net.labymod.gradle.mixin")
}

dependencies {
	annotationProcessor("net.labymod.labymod4:addon-annotation-processor:0.1.0-${project.property("net.labymod.addon-release-type")}")
	api("net.labymod.labymod4:v1_8:0.1.0-${project.property("net.labymod.addon-release-type")}")
}

legacyMinecraft {
	version("1.8.9")
	mappingFile(File(rootProject.projectDir, "mappings/1.8.9.srg"))
}

mixin {
	version("1.8.9")
	addReferenceMap(sourceSets.findByName("main"), "labymod.refmap.json")
}