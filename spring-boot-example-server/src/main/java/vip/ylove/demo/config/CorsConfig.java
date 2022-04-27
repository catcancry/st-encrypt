package vip.ylove.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "cors.allowOrigin.enable", havingValue = "true", matchIfMissing = false)
public class CorsConfig implements WebMvcConfigurer {

  @Value("${cors.allowOrigin.url:anyValue}")
  private String corsAllowOriginUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] corsAllowOriginUrls = corsAllowOriginUrl.split(",");
    if (corsAllowOriginUrls == null
            || corsAllowOriginUrls.length == 1
            || corsAllowOriginUrls[0].trim().isEmpty()) {

      corsAllowOriginUrls = new String[]{"*"};
    }
    log.info("启用跨域，允许域名:" + corsAllowOriginUrl);

    registry.addMapping("/**")
            .allowedOriginPatterns(corsAllowOriginUrls)
            .allowCredentials(true)
            .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600);
  }
}