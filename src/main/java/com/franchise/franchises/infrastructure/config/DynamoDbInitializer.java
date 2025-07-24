package com.franchise.franchises.infrastructure.config;

import com.franchise.franchises.infrastructure.models.BranchEntity;
import com.franchise.franchises.infrastructure.models.BranchProductEntity;
import com.franchise.franchises.infrastructure.models.FranchiseEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.util.concurrent.CompletableFuture;

@Configuration
public class DynamoDbInitializer {
    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public DynamoDbInitializer(DynamoDbEnhancedAsyncClient enhancedAsyncClient, DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    @PostConstruct
    public void init() {
        createTableIfNotExist("Franchise", FranchiseEntity.class);
        createTableIfNotExist("Branch", BranchEntity.class);
        createTableIfNotExist("BranchProduct", BranchProductEntity.class);
    }

    private <T> void createTableIfNotExist(String tableName, Class<T> entityClass) {
        DynamoDbAsyncTable<T> table = enhancedAsyncClient.table(tableName, TableSchema.fromBean(entityClass));

        dynamoDbAsyncClient.listTables()
                .thenCompose(response -> {
                    if(!response.tableNames().contains(tableName)){
                        System.out.println("Creating table " + tableName);
                        return table.createTable();
                    } else {
                        System.out.println("Table " + tableName + " already exists");
                        return CompletableFuture.completedFuture(null);
                    }
                })
                .exceptionally(e -> {
                    System.out.println("Error creating table " + tableName + ": " + e.getMessage());
                    return null;
                });
    }
}
