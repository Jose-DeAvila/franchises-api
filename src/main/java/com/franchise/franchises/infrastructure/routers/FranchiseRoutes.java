package com.franchise.franchises.infrastructure.routers;

import com.franchise.franchises.infrastructure.handlers.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static com.franchise.franchises.infrastructure.constants.Constants.BASE;

@Configuration
public class FranchiseRoutes {
    @Bean
    public RouterFunction<ServerResponse> franchiseRoute(FranchiseHandler franchiseHandler) {
        return route(POST(BASE + "/franchise"), franchiseHandler::save)
                .andRoute(PUT(BASE + "/franchise/{id}"), franchiseHandler::renameById);
    }
}
