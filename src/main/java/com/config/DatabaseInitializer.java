package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    @Autowired
    private DatabaseConfig databaseConfig;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing database...");
        
        Connection connection = null;
        Statement statement = null;
        
        try {
            // Get connection
            connection = databaseConfig.getConnection();
            
            // Ensure auto-commit is enabled (PostgreSQL default, but making sure)
            connection.setAutoCommit(true);
            
            // Verify we're connected to the right database
            String currentDb = connection.getCatalog();
            System.out.println("Connected to database: " + currentDb);
            
            statement = connection.createStatement();
            
            // Read and execute schema.sql
            try {
                ClassPathResource schemaResource = new ClassPathResource("schema.sql");
                String schemaSql = FileCopyUtils.copyToString(
                    new InputStreamReader(schemaResource.getInputStream(), StandardCharsets.UTF_8)
                );
                
                System.out.println("Schema SQL content: " + schemaSql);
                
                // Remove comments and clean up
                schemaSql = schemaSql.replaceAll("--.*", "").trim();
                
                // Execute schema SQL directly (CREATE TABLE IF NOT EXISTS handles duplicates)
                if (!schemaSql.isEmpty()) {
                    statement.execute(schemaSql);
                    System.out.println("Executed CREATE TABLE statement");
                }
                
                // Small delay to ensure commit
                Thread.sleep(100);
                
                // Verify table was created
                boolean tableExists = statement.executeQuery(
                    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'users')"
                ).getBoolean(1);
                
                if (tableExists) {
                    System.out.println("Schema created successfully! Table 'users' exists.");
                } else {
                    System.err.println("WARNING: Table 'users' was not created!");
                    // Try to create it directly as fallback
                    try {
                        String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                            "id SERIAL PRIMARY KEY, " +
                            "email VARCHAR(255) UNIQUE NOT NULL, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "username VARCHAR(255), " +
                            "phone VARCHAR(20)" +
                            ")";
                        statement.execute(createTableSql);
                        System.out.println("Created table using fallback method");
                    } catch (Exception ex) {
                        System.err.println("Fallback table creation also failed: " + ex.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error executing schema.sql: " + e.getMessage());
                e.printStackTrace();
                // Try fallback creation
                try {
                    String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                        "id SERIAL PRIMARY KEY, " +
                        "email VARCHAR(255) UNIQUE NOT NULL, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "username VARCHAR(255), " +
                        "phone VARCHAR(20)" +
                        ")";
                    statement.execute(createTableSql);
                    System.out.println("Created table using fallback method after error");
                } catch (Exception ex) {
                    System.err.println("Fallback table creation failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            
            // Read and execute data.sql
            try {
                ClassPathResource dataResource = new ClassPathResource("data.sql");
                String dataSql = FileCopyUtils.copyToString(
                    new InputStreamReader(dataResource.getInputStream(), StandardCharsets.UTF_8)
                );
                
                System.out.println("Data SQL content: " + dataSql);
                
                // Remove comments and clean up
                dataSql = dataSql.replaceAll("--.*", "").trim();
                
                // Execute data SQL directly
                if (!dataSql.isEmpty()) {
                    statement.execute(dataSql);
                    System.out.println("Executed INSERT statement");
                }
                System.out.println("Data inserted successfully!");
            } catch (Exception e) {
                System.err.println("Error executing data.sql: " + e.getMessage());
                e.printStackTrace();
                // Continue even if data insert fails (might already exist)
            }
            
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}

