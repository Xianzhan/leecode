package com.github.xianzhan.vertx.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/9/6
 */
public class MyFirstVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) throws Exception {
        vertx
                .createHttpServer()
                .requestHandler(req -> {
                    req.response().end("<h1>Hello World</h1>");
                })
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MyFirstVerticle());
    }
}
