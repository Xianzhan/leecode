package com.github.xianzhan.vertx.core.router;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * 描述：返回静态资源给客户端
 *
 * @author Lee
 * @since 2017/9/7
 */
public class RouterWithHtml extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) throws Exception {
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("context-type", "text/html")
                    .end("<h1>Hello my second Router");
        });

        // Serve static resources from the /assets directory
        router.route("/assets/*").handler(StaticHandler.create("assets"));

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
        vertx.deployVerticle(new RouterWithHtml());
        System.out.println("http://localhost:8080/assets/index.html");
    }
}
