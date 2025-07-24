package com.franchise.franchises.infrastructure.handlers;

import com.franchise.franchises.domain.entities.models.Franchise;
import com.franchise.franchises.domain.entities.ports.in.FranchiseServicePort;
import com.franchise.franchises.infrastructure.dto.RenameRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.franchise.franchises.infrastructure.constants.Constants.BASE;
import java.net.URI;

@Component
public class FranchiseHandler {
    private final FranchiseServicePort franchiseServicePort;

    public FranchiseHandler(FranchiseServicePort franchiseServicePort) {
        this.franchiseServicePort = franchiseServicePort;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(Franchise.class)
                .flatMap(franchiseServicePort::save)
                .flatMap(createdFranchise -> ServerResponse
                        .created(URI.create(BASE + "/franchise/" + createdFranchise.id()))
                        .bodyValue("Franchise created successfully"));
    }

    public Mono<ServerResponse> renameById(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(RenameRequest.class)
                .flatMap(body -> franchiseServicePort.renameById(id, body.getNewName()))
                .flatMap(updatedFranchise -> ServerResponse.ok().bodyValue("Franchise updated successfully"));
    }
}
