package com.noscompany.snake.game.online.host.server.nettosphere;

import com.noscompany.snake.game.online.host.server.dto.IpAddress;
import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.IpAddressChecker;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

@AllArgsConstructor
@Slf4j
class HttpAmazonIpAddressChecker implements IpAddressChecker {

    @Override
    public Try<IpAddress> getIpAddress() {
        try {
            String responseBody = httpGet(request()).body();
            return success(new IpAddress(responseBody));
        } catch (IOException | InterruptedException e) {
            return failure(e);
        }
    }

    private HttpResponse<String> httpGet(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest request() {
        return HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create("http://checkip.amazonaws.com"))
                .build();
    }
}