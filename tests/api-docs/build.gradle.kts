dependencies {
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
    testImplementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.assertj:assertj-core:3.25.1")

    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("org.springframework.boot:spring-boot-starter-test")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")

    api("org.springframework.restdocs:spring-restdocs-mockmvc")
    api("org.springframework.restdocs:spring-restdocs-restassured")
    api("io.rest-assured:spring-mock-mvc")
}
