module web {
    // ------------------------- spring-boot-starter --------------------------
    // @SpringBootApplication
    requires spring.boot.autoconfigure;

    // SpringApplication
    requires spring.boot;

    // SQLException
    requires java.sql;

    // 使用反射需要 opens
    opens xianzhan;
    // ------------------------- spring-boot-starter --------------------------
}