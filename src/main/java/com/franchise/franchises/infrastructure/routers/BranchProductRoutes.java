package com.franchise.franchises.infrastructure.routers;

import com.franchise.franchises.infrastructure.handlers.BranchProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static com.franchise.franchises.infrastructure.constants.Constants.BASE;

@Configuration
public class BranchProductRoutes {
    @Bean
    public RouterFunction<ServerResponse> branchProductRoute(BranchProductHandler branchProductHandler) {
        return route(POST(BASE + "/product"), branchProductHandler::save)
                .andRoute(DELETE(BASE + "/product/{branchId}/{productId}"), branchProductHandler::delete)
                .andRoute(PUT(BASE + "/product/{branchId}/{productId}"), branchProductHandler::renameById)
                .andRoute(PUT(  BASE + "/product/{branchId}/{productId}/stock"), branchProductHandler::changeStock)
                .andRoute(GET(BASE + "/product/top/{franchiseId}"), branchProductHandler::getTopProducts);
    }
}
