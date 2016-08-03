/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.Charsets;
import org.junit.runner.RunWith;
import org.mongojack.JacksonDBCollection;
import org.mongojack.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cgomez.model.AbstractModel;
import com.cgomez.service.IClusterService;
import com.cgomez.util.FileUtils;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * The Class AbstractTest.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UnitTestApplicationConfig.class })
@ActiveProfiles("test")
public abstract class AbstractTest {

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /** The cluster service. */
    @Autowired
    protected IClusterService clusterService;

    /** The dataset. */
    protected Collection<DBObject> dataset;
    
    /**
     * Use dataset.
     *
     * @param file the file
     */
    protected void useDataset(String file) {
	dataset = new ArrayList<DBObject>();

	try {

	    DBObject dbObject = (DBObject) JSON.parse(FileUtils.readFileToString(new File(file), Charsets.UTF_8));
	    for (Object object : dbObject.toMap().values()) {
		dataset.add((DBObject) object);
	    }

	} catch (IOException e) {
	    assertEquals("Can't be read the file: " + file, "error", "");
	}
    }

    /**
     * Use dataset.
     * 
     * @param file
     *            the file
     * @param type
     *            the type
     */
    protected void useDataset(String file, Class<? extends AbstractModel> type) {
	useDataset(file);
	for (DBObject dBObject : dataset) {
	    mongoTemplate.getCollection(type.getAnnotation(MongoCollection.class).name()).insert(dBObject);
	}
    }

    /**
     * Count.
     * 
     * @param type
     *            the type
     * @return the long
     */
    protected long count(Class<? extends AbstractModel> type) {
	return mongoTemplate.getCollection(type.getAnnotation(MongoCollection.class).name()).count();
    }

    /**
     * Find all.
     * 
     * @param type
     *            the type
     * @return the collection
     */
    @SuppressWarnings("unchecked")
    protected Collection<AbstractModel> findAll(Class<? extends AbstractModel> type) {
	return (Collection<AbstractModel>) (Collection<?>) getCollection(type).find().toArray();
    }

    /**
     * Gets the collection.
     * 
     * @param type
     *            the type
     * @return the collection
     */
    @SuppressWarnings("unchecked")
    public JacksonDBCollection<AbstractModel, String> getCollection(Class<? extends AbstractModel> type) {
	return (JacksonDBCollection<AbstractModel, String>) JacksonDBCollection.wrap(
		mongoTemplate.getCollection(type.getAnnotation(MongoCollection.class).name()), type, String.class);
    }
}