/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import com.cgomez.AbstractTest;
import com.cgomez.model.AbstractModel;
import com.cgomez.model.bibliography.BibliographicCitationDBLP;
import com.cgomez.util.constant.BibliographicCitationEnum;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

/**
 * The Class BatchDBLPTest.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
public class BatchDBLPTest extends AbstractTest {

    /** The Constant KEY. */
    private static final String KEY = "persons/Codd74";
    
    /** The Constant FILE. */
    private static final String FILE = "src/test/resources/files/dblp/sample.xml";
    
    /** The Constant COUNT_FILE. */
    private static final int COUNT_FILE = 5;
    
    /** The Constant DATASET. */
    private static final String DATASET = "src/test/resources/dataset/bibliographic_citations.json";
    
    /** The batch. */
    @Autowired
    private BatchDBLP batch;

    /**
     * Run.
     *
     * @throws ParseException the parse exception
     */
    @Test
    @DirtiesContext
    public void run() throws ParseException {
	useDataset(DATASET);
	batch.execute(FILE);

	Collection<AbstractModel> bibliographicCitations = findAll(BibliographicCitationDBLP.class);
	assertEquals(COUNT_FILE, bibliographicCitations.size());
	
	BibliographicCitationDBLP bibliographicCitation = null;
	for (AbstractModel abstractModel : bibliographicCitations) {
	    bibliographicCitation = (BibliographicCitationDBLP) abstractModel;
	    if (bibliographicCitation.getKey().equals(KEY)) {
		break;
	    }
	}

	DBObject dbObject = dataset.iterator().next();
	assertEquals(dbObject.get("mdate"), bibliographicCitation.getMdate());
	assertEquals(((BasicDBList) dbObject.get("authors")).size(), bibliographicCitation.getAuthors().size());
	assertEquals(((BasicDBList) dbObject.get("authors")).get(0), bibliographicCitation.getAuthors().get(0));
	assertEquals(dbObject.get("title"), bibliographicCitation.getTitle());
	assertEquals(dbObject.get("booktitle"), bibliographicCitation.getBooktitle());
	assertEquals(dbObject.get("pages"), bibliographicCitation.getPages());
	assertEquals(dbObject.get("year"), bibliographicCitation.getYear());
	assertEquals(dbObject.get("month"), bibliographicCitation.getMonth());
	assertEquals(dbObject.get("url"), bibliographicCitation.getUrl());
	assertEquals(dbObject.get("cdrom"), bibliographicCitation.getCdrom());
	assertEquals(dbObject.get("note"), bibliographicCitation.getNote());
	assertEquals(BibliographicCitationEnum.INPROCEEDINGS, bibliographicCitation.getType());
    }
}