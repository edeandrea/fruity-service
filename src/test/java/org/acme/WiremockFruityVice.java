package org.acme;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WiremockFruityVice implements QuarkusTestResourceLifecycleManager {
	private WireMockServer server;

	@Override
	public Map<String, String> start() {
		this.server = new WireMockServer(
			options()
				.notifier(new Slf4jNotifier(true))
		);

		this.server.start();

		stubFor(
			get(urlEqualTo("/api/fruit/Apple"))
				.willReturn(
					aResponse()
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
						.withBody("{\"family\":\"Rosaceae\",\"genus\":\"Malus\",\"id\":6,\"name\":\"Apple\",\"nutritions\":{\"calories\":52,\"carbohydrates\":11.4,\"fat\":0.4,\"protein\":0.3,\"sugar\":10.3},\"order\":\"Rosales\"}")
				)
		);

		stubFor(
			get(urlEqualTo("/api/fruit/Pear"))
				.willReturn(
					aResponse()
						.withStatus(Status.NOT_FOUND.getStatusCode())
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
						.withBody("{\"error\":\"The fruit was not found\"}")
				)
		);

		stubFor(
			get(urlEqualTo("/api/fruit/Lemon"))
				.willReturn(
					aResponse()
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
						.withBody("{\"family\":\"Rutaceae\",\"genus\":\"Citrus\",\"id\":7,\"name\":\"Lemon\",\"nutritions\":{\"calories\":29,\"carbohydrates\":9,\"fat\":0.3,\"protein\":1.1,\"sugar\":2.5},\"order\":\"Sapindales\"}")
				)
		);

		return Map.of("fruity-vice/mp-rest/url", this.server.baseUrl());
	}

	@Override
	public void stop() {
		Optional.ofNullable(this.server)
			.ifPresent(WireMockServer::stop);
	}
}
