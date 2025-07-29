package com.franchise.franchises.infrastructure.handlers;

import com.franchise.franchises.domain.entities.exceptions.EntityNotFound;
import com.franchise.franchises.domain.entities.models.Branch;
import com.franchise.franchises.domain.entities.ports.in.BranchServicePort;
import com.franchise.franchises.infrastructure.dto.RenameRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.franchise.franchises.infrastructure.constants.Constants.BASE;
import java.net.URI;

@Component
public class BranchHandler {
    private final BranchServicePort branchServicePort;

    public BranchHandler(BranchServicePort branchServicePort) {
        this.branchServicePort = branchServicePort;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(Branch.class)
                .flatMap(branchServicePort::save)
                .flatMap(createdBranch -> ServerResponse
                        .created(URI.create(BASE + "/branch/" + createdBranch.id()))
                        .bodyValue("Branch created successfully"))
                .onErrorResume(EntityNotFound.class, e -> ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(e.getMessage()))
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue("Error creating the branch: " + e.getMessage()));
    }

    public Mono<ServerResponse> renameById(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(RenameRequest.class)
                .flatMap(body -> branchServicePort.renameById(id, body.getNewName()))
                .flatMap(updatedBranch -> ServerResponse.ok().bodyValue("Branch updated successfully"))
                .onErrorResume(EntityNotFound.class, e -> ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(e.getMessage()))
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue("Error renaming the branch: " + e.getMessage()));
    }
}
