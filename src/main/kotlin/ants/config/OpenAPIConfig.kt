package ants.config

import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

  @Bean
  fun hideEmptyInterface(): OpenApiCustomizer {
    return OpenApiCustomizer { openApi ->
      openApi.components?.schemas?.remove("Response")
    }
  }
}
