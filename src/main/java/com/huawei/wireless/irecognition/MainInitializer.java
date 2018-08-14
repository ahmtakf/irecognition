package com.huawei.wireless.irecognition;

import com.huawei.wireless.irecognition.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.huawei.wireless.irecognition.entity"})  // scan JPA entities
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class MainInitializer implements CommandLineRunner {

    @Autowired
    StorageService storageService;

    public static void main(String[] args) {

        SpringApplication.run(MainInitializer.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
       // storageService.deleteAll();
       // storageService.init();
    }

}
