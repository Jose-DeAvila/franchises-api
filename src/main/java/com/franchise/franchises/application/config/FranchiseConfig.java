package com.franchise.franchises.application.config;

import com.franchise.franchises.domain.entities.ports.in.FranchiseServicePort;
import com.franchise.franchises.domain.entities.ports.out.FranchisePersistencePort;
import com.franchise.franchises.domain.entities.usecases.FranchiseUseCase;
import com.franchise.franchises.infrastructure.adapters.FranchiseAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Configuration
public class FranchiseConfig {
    @Bean
    public FranchisePersistencePort franchisePersistencePort(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        return new FranchiseAdapter(enhancedAsyncClient);
    }

    @Bean
    public FranchiseServicePort franchiseServicePort(FranchisePersistencePort franchisePersistencePort) {
        return new FranchiseUseCase(franchisePersistencePort);
    }
}
