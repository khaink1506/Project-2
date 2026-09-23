package com.devon.building.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Component
public class OAuth2PictureFetcher {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public byte[] fetchGoogleProfilePicture(String pictureUrl){
        if(!StringUtils.isNotBlank(pictureUrl)){
            return null;
        }

        URI uri = URI.create(pictureUrl.trim());
        if(!isAllowedPictureHost(uri)){
            log.warn("Reject profile picture URL: " + uri.getHost());
            return null;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if(response.statusCode() >= 200 && response.statusCode() < 300){
                byte[] body = response.body();
                return (body != null && body.length > 0) ? body : null;
            }
        } catch (Exception e) {
            log.warn("Failed to download OAuth2 profile picture: " + e.getMessage(), e);
        }
        return null;
    }

    private boolean isAllowedPictureHost(URI uri){
        if(!"https".equalsIgnoreCase(uri.getScheme())){
            return false;
        }
        String host = uri.getHost();
        if(host == null){
            return false;
        }
        return host.endsWith("googleusercontent.com") || host.equals("lh3.googleusercontent.com");
    }
}
