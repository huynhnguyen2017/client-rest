package org.sample.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class WireMockExtensions implements QuarkusTestResourceLifecycleManager {

    private static final String COUNTRIES_JSON_FILE = "extensions.json";
    private static final String BASE_PATH = "/api";
    private static final int WIREMOCK_PORT = 7777;

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(WIREMOCK_PORT);
        wireMockServer.start();
        stubExtensions();
        return Collections.singletonMap("quarkus.rest-client.\"org.sample.ExtensionService\".url",
                wireMockServer.baseUrl() + BASE_PATH);
    }

    @Override
    public void stop() {
        if (Objects.nonNull(wireMockServer)) {
            wireMockServer.stop();
        }
    }

    private void stubExtensions() {
        try(InputStream is = WireMockExtensions.class.getResourceAsStream(COUNTRIES_JSON_FILE)) {
            String extensions = new String(is.readAllBytes());

            wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(BASE_PATH))
                    .willReturn(WireMock.okJson(extensions)));

            // stub for each country
            try(StringReader sr = new StringReader(extensions); JsonParser parser = Json.createParser(sr)) {
                parser.next();
                for (JsonValue extension : parser.getArray()) {
                    String id = extension.asJsonObject().getString("id");

                    wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(BASE_PATH + "/extensions?id="
                            + URLEncoder.encode(id, StandardCharsets.UTF_8)))
                            .willReturn(ResponseDefinitionBuilder.okForJson("[" + extension + "]")));
                }
            }

        } catch (IOException ex) {
            fail("Could not configure Wiremock server. Caused by: " + ex.getMessage());
        }
    }
}
