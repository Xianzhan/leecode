package com.github.xianzhan.vertx.core.router;

import com.github.xianzhan.vertx.core.first.MyFirstVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * 描述：By vertx-web
 *
 * @author Lee
 * @since 2017/9/7
 */
public class MyFirstRouter extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) throws Exception {
        // Create a router object
        Router router = Router.router(vertx);

        // Bind "/" to our hello message
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html")
                    .end("<h1>Hello Router</h1>");
        });

        // Create the HTTP server
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MyFirstVerticle());
        System.out.println("Server is START");
    }
}
