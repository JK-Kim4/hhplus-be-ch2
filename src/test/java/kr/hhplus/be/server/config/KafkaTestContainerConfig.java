package kr.hhplus.be.server.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;

@Configuration
public class KafkaTestContainerConfig {

    public static final KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka")
    ).withReuse(false);

    static {
        kafkaContainer.start();

        // KafkaContainer.getBootstrapServers() → "PLAINTEXT://localhost:53307"
        // Spring Boot은 "localhost:53307" 형태를 기대
        String bootstrapServers = kafkaContainer.getBootstrapServers()
                .replace("PLAINTEXT://", "");

        System.setProperty("spring.kafka.bootstrap-servers", bootstrapServers);
    }


    public static AdminClient getAdminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers()
                .replace("PLAINTEXT://", ""));
        return AdminClient.create(props);
    }
}
