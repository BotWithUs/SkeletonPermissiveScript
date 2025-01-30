
plugins {
    id("java")
}

group = "net.botwithus.scripts"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
//    maven {
//        setUrl("https://nexus.botwithus.net/repository/maven-snapshots/")
//    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

configurations {
    create("includeInJar") {
        this.isTransitive = false
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "22"
    targetCompatibility = "22"
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.flogger:flogger:0.7.4")
    implementation("net.botwithus.api:api:1.0.0-SNAPSHOT")
    implementation("net.botwithus.xapi:xapi:1.0.0-SNAPSHOT")
    implementation("net.botwithus.imgui:imgui:1.0.0-SNAPSHOT")
    implementation("org.projectlombok:lombok:1.18.26")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

val copyJar by tasks.register<Copy>("copyJar") {
    from(tasks.withType<Jar>())
    into("${System.getProperty("user.home")}\\.botwithus\\scripts\\")
    include("*.jar")
}

tasks.named<Jar>("jar") {
    from({
        configurations["includeInJar"].map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    finalizedBy(copyJar)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}