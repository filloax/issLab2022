import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * This file was generated by the Gradle 'init' task.
 * and updated by AN
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.7.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    jacoco
    distribution
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    flatDir{ dirs("../../unibolibs")   }   //Our libraries (we are in app ...)
}

version = "1.0"
//sourceCompatibility = 15

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

//COROUTINE
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")

//OkHttp library for websockets with Kotlin
    implementation( "com.squareup.okhttp3:okhttp:4.9.0" )
//JSON
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20220320" )
//STRING COLORS
    implementation( "com.andreapivetta.kolor:kolor:1.0.0" )
//UNIBO
    implementation( "uniboInterfaces:uniboInterfaces"  )
    implementation( "uniboProtocolSupport:unibonoawtsupports"  )
    //implementation( "uniboIssSupport:IssActorKotlinRobotSupportJ11-2.0")
}

application {
    // Define the main class for the application.
    mainClass.set("demo2022Kt")
}


/*
val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "it.unibo.actor0Robot.MainStepRobotServiceKt"
    }
}
*/
tasks.jar {
    manifest {
        attributes["Main-Class"] = "it.unibo.actor0robot.MainStepRobotServiceKt"
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

/*
val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        //attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "it.unibo.actor0Robot.MainStepRobotServiceKt"
    }
    //from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
    from(configurations.runtime.map({  zipTree(it) }))
    with(tasks["jar"] as CopySpec)
}*/