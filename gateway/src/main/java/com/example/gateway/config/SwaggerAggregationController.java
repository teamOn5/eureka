package com.example.gateway.config;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Eureka에 등록된 서비스들의 OpenAPI 문서를 Swagger UI 드롭다운에 노출한다.
 * GET /swagger-services-config
 */
@RestController
public class SwaggerAggregationController {

	private static final Set<String> EXCLUDED_SERVICES = Set.of("gateway", "eureka");

	private final DiscoveryClient discoveryClient;

	public SwaggerAggregationController(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	@GetMapping("/swagger-services-config")
	public Map<String, Object> swaggerServicesConfig() {
		List<Map<String, String>> urls = discoveryClient.getServices().stream()
				.filter(service -> EXCLUDED_SERVICES.stream()
						.noneMatch(excluded -> excluded.equalsIgnoreCase(service)))
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.map(service -> {
					String serviceId = service.toLowerCase();
					Map<String, String> entry = new LinkedHashMap<>();
					entry.put("name", serviceId);
					entry.put("url", "/" + serviceId + "/v3/api-docs");
					return entry;
				})
				.toList();

		Map<String, Object> config = new LinkedHashMap<>();
		config.put("urls", urls);
		config.put("validatorUrl", "");
		if (!urls.isEmpty()) {
			config.put("urls.primaryName", urls.get(0).get("name"));
		}
		return config;
	}
}
