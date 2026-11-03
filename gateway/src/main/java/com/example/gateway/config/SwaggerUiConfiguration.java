package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.webjars.WebJarVersionLocator;

import java.net.URI;

/**
 * Gateway에서 /swagger-ui/index.html 로 통합 문서를 제공한다.
 * webjars 기본 initializer(Petstore) 대신 Eureka 기반 설정을 주입한다.
 */
@Configuration
public class SwaggerUiConfiguration {

	private static final String SWAGGER_UI_WEBJAR = "swagger-ui";

	@Bean
	public RouterFunction<ServerResponse> swaggerUiRoutes() {
		WebJarVersionLocator locator = new WebJarVersionLocator();
		String version = locator.version(SWAGGER_UI_WEBJAR);
		if (version == null || version.isBlank()) {
			throw new IllegalStateException("classpath에서 swagger-ui webjar를 찾을 수 없습니다.");
		}

		ClassPathResource swaggerUiLocation = new ClassPathResource(
				"META-INF/resources/webjars/swagger-ui/" + version + "/");

		String initializer = """
				window.onload = function() {
				  fetch('/swagger-services-config')
				    .then(function(response) { return response.json(); })
				    .then(function(config) {
				      var options = {
				        dom_id: '#swagger-ui',
				        deepLinking: true,
				        presets: [
				          SwaggerUIBundle.presets.apis,
				          SwaggerUIStandalonePreset
				        ],
				        plugins: [
				          SwaggerUIBundle.plugins.DownloadUrl
				        ],
				        layout: 'StandaloneLayout',
				        validatorUrl: null
				      };

				      if (config.urls && config.urls.length > 0) {
				        options.urls = config.urls;
				        options['urls.primaryName'] = config.urls[0].name;
				      }

				      window.ui = SwaggerUIBundle(options);
				    })
				    .catch(function(error) {
				      console.error('Failed to load swagger services config', error);
				      window.ui = SwaggerUIBundle({
				        dom_id: '#swagger-ui',
				        deepLinking: true,
				        presets: [
				          SwaggerUIBundle.presets.apis,
				          SwaggerUIStandalonePreset
				        ],
				        plugins: [
				          SwaggerUIBundle.plugins.DownloadUrl
				        ],
				        layout: 'StandaloneLayout',
				        validatorUrl: null,
				        spec: {
				          openapi: '3.0.0',
				          info: {
				            title: 'No services registered',
				            description: 'Eureka에 등록된 서비스가 없습니다. 서비스 기동 후 새로고침하세요.',
				            version: '0.0.0'
				          },
				          paths: {}
				        }
				      });
				    });
				};
				""";

		return RouterFunctions.route()
				.GET("/swagger-ui/swagger-initializer.js",
						request -> ServerResponse.ok()
								.contentType(MediaType.valueOf("text/javascript"))
								.bodyValue(initializer))
				.GET("/swagger-ui.html",
						request -> ServerResponse.temporaryRedirect(URI.create("/swagger-ui/index.html")).build())
				.GET("/swagger-ui",
						request -> ServerResponse.temporaryRedirect(URI.create("/swagger-ui/index.html")).build())
				.resources("/swagger-ui/**", swaggerUiLocation)
				.build();
	}
}
