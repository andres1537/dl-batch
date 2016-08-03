/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.stereotype.Component;

import com.cgomez.exception.DLBusinessServiceException;
import com.cgomez.model.author.Cluster;
import com.cgomez.model.bibliography.BibliographicCitationLeeGiles;
import com.cgomez.util.FileUtils;
import com.cgomez.util.constant.Constant;
import com.cgomez.util.helper.LeeGilesDataHelper;

/**
 * The Class BatchLeeGilesData.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
@Component
public final class BatchLeeGilesData extends AbstractBatch {
    
    /** The Constant SEPARATOR. */
    public static final String SEPARATOR = "<>";
    
    /**
     * Execute.
     * 
     * @param dataFile
     *            the data file
     */
    public void execute(String[] dataFiles) {
	for (String dataFile : dataFiles) {
	    processFile(dataFile);
	}
    }

    /**
     * Process file.
     */
    private void processFile(String dataFile) {
	LOG.info(dataFile);
	LineIterator it = null;

	try {

	    final File file = new File(dataFile);
	    it = FileUtils.lineIterator(file, CharEncoding.UTF_8);
	    String line = null;
	    String[] tokens = null;
	    
	    while (it.hasNext()) {
		line = it.nextLine();
		tokens = line.split(SEPARATOR);
		if (tokens.length > 3 || tokens.length < 2) {
		    LOG.error(line);

		} else {
		    // cluster
		    Cluster cluster = LeeGilesDataHelper.extractCluster(dataFile, tokens[0]);
		    Cluster clusterDB = clusterService.retrieveByKey(cluster.getKey());
		    if (clusterDB == null) {
			clusterService.create(cluster);

		    } else {
			cluster = clusterDB;
		    }

		    // bibliographic citation dblp
		    BibliographicCitationLeeGiles bibliographicCitationLeeGiles = new BibliographicCitationLeeGiles();
		    bibliographicCitationLeeGiles.setAuthors(LeeGilesDataHelper.extractAuthors(tokens[0]));
		    bibliographicCitationLeeGiles.setTitle(tokens[1].trim());
		    bibliographicCitationLeeGiles.setJournal(tokens.length == 3 ? tokens[2] : Constant.EMPTY.trim());
		    bibliographicCitationLeeGiles.setGroupId(cluster.getKey());
		    bibliographicCitationLeeGilesService.create(bibliographicCitationLeeGiles);
		}
	    }

	} catch (IOException e) {
	    LOG.error(e.getMessage());

	} catch (DLBusinessServiceException e) {
	    LOG.error(e.getMessage());

	} finally {
	    LineIterator.closeQuietly(it);
	}
    }
}