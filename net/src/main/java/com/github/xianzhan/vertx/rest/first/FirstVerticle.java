package com.github.xianzhan.vertx.rest.first;

import com.github.xianzhan.vertx.rest.bean.Whisky;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/9/9
 */
public class FirstVerticle extends AbstractVerticle {

    // Store our product
    private Map<Integer, Whisky> products = new LinkedHashMap<>();

    // Create some product
    private void createSomeData() {
        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, " +
                "Islay");
        products.put(bowmore.getId(), bowmore);
        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, " +
                "Island");
        products.put(talisker.getId(), talisker);
    }

    // 获取全部瓶子
    private void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json;charset=utf-8")
                .end(Json.encodePrettily(products.values()));
    }

    // 添加一个瓶子
    private void addOne(RoutingContext context) {
        final Whisky whisky = Json.decodeValue(context.getBodyAsString(),
                                               Whisky.class);
        products.put(whisky.getId(), whisky);
        context.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json;charset=utf-8")
                .end(Json.encodePrettily(whisky));
    }

    // 删除一个瓶子
    private void deleteOne(RoutingContext context) {
        String id = context.request().getParam("id");
        if (id == null) {
            context.response().setStatusCode(400).end();
        } else {
            Integer idAsInteger = Integer.valueOf(id);
            products.remove(idAsInteger);
        }
        context.response().setStatusCode(204).end();
    }

    // 获取一个瓶子
    private void getOne(RoutingContext context) {
        String id = context.request().getParam("id");
        if (id == null) {
            context.response().setStatusCode(204).end();
        } else {
            Integer idAsInteger = Integer.valueOf(id);
            Whisky whisky = products.get(idAsInteger);
            context.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json;" +
                            "charset=utf-8")
                    .end(Json.encodePrettily(whisky));
        }
    }

    @Override
    public void start(Future<Void> fut) throws Exception {
        // 启动时创建两个瓶子
        createSomeData();
        // Create a router object.
        Router router = Router.router(vertx);

        // Rest of the method
        // 获取全部的瓶子
        router.get("/api/whiskies").handler(this::getAll);
        // 添加一个瓶子
        router.route("/api/whiskies").handler(BodyHandler.create());
        router.post("/api/whiskies").handler(this::addOne);
        // 删除一个瓶子
        router.delete("/api/whiskies/:id").handler(this::deleteOne);
        // 获取一个瓶子
        router.get("/api/whiskies/:id").handler(this::getOne);

        // Create a Server
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
        vertx.deployVerticle(new FirstVerticle());
    }
}
