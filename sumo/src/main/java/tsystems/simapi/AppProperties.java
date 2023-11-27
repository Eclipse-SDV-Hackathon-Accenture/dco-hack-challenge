package tsystems.simapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;


/**
 * Properties of application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * The cors properties.
     */
    @NestedConfigurationProperty
    private Cors cors;


    /**
     * Properties of cors.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cors {

        private String headers;
        private String origins;
    }

}
