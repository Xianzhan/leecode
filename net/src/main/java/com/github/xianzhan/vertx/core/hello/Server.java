package com.github.xianzhan.vertx.core.hello;

import com.github.xianzhan.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * 描述：Vertx Hello World Server
 *
 * @author Lee
 * @since 2017/9/5
 */
public class Server extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader("content-type", "text/html")
                    .end("<html><body><h1>Hello World</h1></body></html>");
        }).listen(8080);
    }

    public static void main(String[] args) {
        Runner.runExample(Server.class);
    }
}
