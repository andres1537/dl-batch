/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import com.cgomez.AbstractTest;
import com.cgomez.exception.DLBusinessServiceException;
import com.cgomez.model.AbstractModel;
import com.cgomez.model.author.Cluster;
import com.cgomez.model.bibliography.BibliographicCitationLeeGiles;

/**
 * The Class BatchLeeGilesDataTest.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
public class BatchLeeGilesDataTest extends AbstractTest {

    /** The Constant TITLE. */
    private static final String TITLE = "PMES: privilege mangagement and enforcement system for secure distributed resource sharing";

    /** The Constant JOURNAL. */
    private static final String JOURNAL = "IFIP International Federation for Information Processing World Conference IT Tools";

    /** The Constant AUTHORS. */
    private static final String[] AUTHORS = { "A Gupta", "Bjorn Kvande", "Irwin B Levinstein", "Kurt Maly", "Margrethe H Olson", "Ravi Mukkamala", "Rita Chambers", "Roy Whitney", "S Nanjangud" };

    /** The Constant CLUSTER_KEY. */
    private static final String CLUSTER_KEY = "sample_10";
    
    /** The Constant FILES. */
    private static final String[] FILES = { "src/test/resources/files/clgilesData/sample.txt" };
    
    /** The batch lee giles data. */
    @Autowired
    private BatchLeeGilesData batch;

    /**
     * Run.
     */
    @Test
    @DirtiesContext
    public void run() {
	batch.execute(FILES);
	assertEquals(10, count(BibliographicCitationLeeGiles.class));

	try {

	    Collection<AbstractModel> bibliographicCitationsLeeGiles = findAll(BibliographicCitationLeeGiles.class);
	    BibliographicCitationLeeGiles bibliographicCitationLeeGiles = null;
	    for (AbstractModel abstractModel : bibliographicCitationsLeeGiles) {
		bibliographicCitationLeeGiles = (BibliographicCitationLeeGiles) abstractModel;
		if (bibliographicCitationLeeGiles.getTitle().equals(TITLE)) {
		    break;
		}
	    }

	    Cluster cluster = clusterService.retrieveByKey(CLUSTER_KEY);
	    assertEquals(cluster.getKey(), bibliographicCitationLeeGiles.getGroupId());
	    assertEquals(JOURNAL, bibliographicCitationLeeGiles.getJournal());

	    assertEquals(AUTHORS.length, bibliographicCitationLeeGiles.getAuthors().size());
	    for (int i = 0; i < AUTHORS.length; i++) {
		assertEquals(AUTHORS[i], bibliographicCitationLeeGiles.getAuthors().get(i));
	    }

	} catch (DLBusinessServiceException e) {
	    assertEquals(e.getMessage(), "error", "");
	}
    }
}