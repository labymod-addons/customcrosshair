import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))

    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.compileTestJava {
    // The Laby annotation processors only get their options for the main sources
    options.compilerArgs.add("-proc:none")
}

tasks.test {
    useJUnitPlatform()
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}