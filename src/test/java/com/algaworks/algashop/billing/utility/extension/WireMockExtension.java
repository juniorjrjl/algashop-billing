package com.algaworks.algashop.billing.utility.extension;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.TemplateEngine;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.util.Objects.isNull;

public class WireMockExtension implements BeforeAllCallback {

    private static WireMockServer server;

    @Override
    public void beforeAll(final ExtensionContext context) {
        if (isNull(server)) {
            server = new WireMockServer(options().port(8788)
                    .usingFilesUnderDirectory("src/test/resources/wiremock/fastpay")
                    .extensions(new ResponseTemplateTransformer(
                            TemplateEngine.defaultTemplateEngine(),
                            true,
                            new ClasspathFileSource("src/test/resources/wiremock/fastpay"),
                            Collections.emptyList())));
            
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        }
    }

}