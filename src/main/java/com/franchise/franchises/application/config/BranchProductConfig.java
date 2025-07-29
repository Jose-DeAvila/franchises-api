package com.franchise.franchises.application.config;

import com.franchise.franchises.domain.entities.ports.in.BranchProductServicePort;
import com.franchise.franchises.domain.entities.ports.out.BranchProductPersistencePort;
import com.franchise.franchises.domain.entities.usecases.BranchProductUseCase;
import com.franchise.franchises.infrastructure.adapters.BranchProductAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Configuration
public class BranchProductConfig {
    @Bean
    public BranchProductPersistencePort branchProductPersistencePort(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        return new BranchProductAdapter(enhancedAsyncClient);
    }

    @Bean
    public BranchProductServicePort branchProductServicePort(BranchProductPersistencePort branchProductPersistencePort) {
        return new BranchProductUseCase(branchProductPersistencePort);
    }
}
