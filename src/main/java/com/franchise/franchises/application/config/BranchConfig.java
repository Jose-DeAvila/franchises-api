package com.franchise.franchises.application.config;

import com.franchise.franchises.domain.entities.ports.in.BranchServicePort;
import com.franchise.franchises.domain.entities.ports.out.BranchPersistencePort;
import com.franchise.franchises.domain.entities.usecases.BranchUseCase;
import com.franchise.franchises.infrastructure.adapters.BranchAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Configuration
public class BranchConfig {
    @Bean
    public BranchPersistencePort branchPersistencePort(DynamoDbEnhancedAsyncClient enhancedAyncClient) {
        return new BranchAdapter(enhancedAyncClient);
    }

    @Bean
    public BranchServicePort branchServicePort(BranchPersistencePort branchPersistencePort) {
        return new BranchUseCase(branchPersistencePort);
    }
}
