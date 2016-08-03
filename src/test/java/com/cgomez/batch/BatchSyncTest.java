/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import com.cgomez.AbstractTest;

/**
 * The Class BatchSyncTest.
 *
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
public class BatchSyncTest extends AbstractTest {

    /** The Constant FILES. */
    private static final String[] FILES = { "src/test/resources/files/clgilesData/sample.txt" };
    
    /** The batch. */
    @Autowired
    private BatchSync batch;

    /**
     * Run.
     */
    @Test
    @DirtiesContext
    public void run() {
//	batch.execute(FILES);
//	assertEquals(10, count(BibliographicCitationDBLP.class));
    }
}