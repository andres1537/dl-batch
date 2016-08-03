/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;

/**
 * The Class UnitTestApplicationConfig.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
@Configuration
@ImportResource("classpath:dl-batch-context.xml")
@EnableMongoRepositories
@ComponentScan
@Profile("test")
public class UnitTestApplicationConfig extends AbstractMongoConfiguration {

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getDatabaseName()
     */
    @Override
    protected String getDatabaseName() {
        return "mongodb-test";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo()
     */
    @Override
    public Mongo mongo() throws Exception {
        return new Fongo(getDatabaseName()).getMongo();
    }
}