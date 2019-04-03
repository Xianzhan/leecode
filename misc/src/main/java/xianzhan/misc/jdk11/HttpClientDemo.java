package xianzhan.misc.jdk11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * 描述：标准 HTTP 客户端
 *
 * @author Lee
 * @since 2019-04-03
 */
public class HttpClientDemo {

    private static void syncGET(String uri) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(uri)
        ).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    private static void asyncGET(String uri) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(uri)
        ).build();

        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        future.whenComplete((resp, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            } else {
                System.out.println(resp.statusCode());
                System.out.println(resp.body());
            }
        }).join();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String uri = "http://t.weather.sojson.com/api/weather/city/101030100";
        syncGET(uri);
        asyncGET(uri);
    }
}
