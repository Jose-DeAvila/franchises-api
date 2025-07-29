package com.franchise.franchises.infrastructure.handlers;

import com.franchise.franchises.domain.entities.exceptions.EntityNotFound;
import com.franchise.franchises.domain.entities.models.BranchProduct;
import com.franchise.franchises.domain.entities.ports.in.BranchProductServicePort;
import com.franchise.franchises.infrastructure.dto.ProductStockResponse;
import com.franchise.franchises.infrastructure.dto.RenameRequest;
import com.franchise.franchises.infrastructure.dto.UpdateStockRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.franchise.franchises.infrastructure.constants.Constants.BASE;
import java.net.URI;

@Component
public class BranchProductHandler {
    private final BranchProductServicePort branchProductServicePort;

    public BranchProductHandler(BranchProductServicePort branchProductServicePort) {
        this.branchProductServicePort = branchProductServicePort;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(BranchProduct.class)
                .flatMap(branchProductServicePort::save)
                .flatMap(createdProduct -> ServerResponse
                        .created(URI.create(BASE + "/product/" + createdProduct.productId()))
                        .bodyValue("Product created successfully"))
                .onErrorResume(EntityNotFound.class, e -> ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(e.getMessage()))
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue("Error adding the product: " + e.getMessage()));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return branchProductServicePort.deleteById(branchId, productId)
                .then(ServerResponse.ok().bodyValue("Product deleted successfully"))
                .onErrorResume(EntityNotFound.class, e -> ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(e.getMessage()))
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue("Error deleting the product: " + e.getMessage()));
    }

    public Mono<ServerResponse> renameById(ServerRequest request) {
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return request.bodyToMono(RenameRequest.class)
                .flatMap(body -> branchProductServicePort
                        .renameById(branchId, productId, body.getNewName()))
                .flatMap(updatedProduct -> ServerResponse
                        .ok()
                        .bodyValue("Product updated successfully"))
                .onErrorResume(EntityNotFound.class, e -> ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(e.getMessage()))
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue("Error renaming the product: " + e.getMessage()));
    }

    public Mono<ServerResponse> changeStock(ServerRequest request) {
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(body -> branchProductServicePort
                        .changeStockById(branchId, productId, body.getDeltaStock()))
                .flatMap(updatedProduct -> ServerResponse
                        .ok()
                        .bodyValue("Stock updated successfully. Current stock: " + updatedProduct.stock()))
                .onErrorResume(EntityNotFound.class, e -> ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(e.getMessage()))
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue("Error changing product stock: " + e.getMessage()));
    }

    public Mono<ServerResponse> getTopProducts(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");

        return branchProductServicePort.getTopProductByStockPerBranch(franchiseId)
                .map(product -> new ProductStockResponse(
                        product.branchId(),
                        product.productId(),
                        product.name(),
                        product.stock()
                        ))
                .collectList()
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
