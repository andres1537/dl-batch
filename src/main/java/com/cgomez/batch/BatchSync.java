/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cgomez.exception.DLBusinessServiceException;
import com.cgomez.exception.DLIOException;
import com.cgomez.model.author.Cluster;
import com.cgomez.model.bibliography.BibliographicCitationDBLP;
import com.cgomez.model.bibliography.BibliographicCitationSynchronized;
import com.cgomez.util.FileUtils;
import com.cgomez.util.helper.BeansHelper;
import com.cgomez.util.helper.LeeGilesDataHelper;

/**
 * The Class BatchSync.
 *
 * @author <a href="mailto:andres1537@gmail.com">Carlos A. Gómez</a>
 * @since dl-batch-1.0
 */
@Component
public final class BatchSync extends AbstractBatch {

    /** The bibliographicCitations file. */
    @Value("${bibliographicCitations.file}")
    private String bibliographicCitationsFile;

    /** The clusters file. */
    @Value("${clusters.file}")
    private String clustersFile;

    /**
     * Execute.
     *
     * @param dataFiles the data files
     */
    public void execute(String[] dataFiles) {
	for (String dataFile : dataFiles) {
	    processFile(dataFile);
	}
	
	generateTxtFile();
    }

    /**
     * Process file.
     *
     * @param dataFile the data file
     */
    private void processFile(String dataFile) {
	LOG.info(dataFile + " --------------------------");
	LineIterator it = null;

	try {

	    final File file = new File(dataFile);
	    it = FileUtils.lineIterator(file, CharEncoding.UTF_8);
	    while (it.hasNext()) {
		String line = it.nextLine();
		String[] tokens = line.split(BatchLeeGilesData.SEPARATOR);
		if (tokens.length > 3 || tokens.length < 2) {
		    LOG.error(line);

		} else {
		    LOG.info("title: " + tokens[1].trim());
		    BibliographicCitationDBLP bibliographicCitationDBLP = bibliographicCitationDBLPService.retrieveMostSimilar(tokens[1].trim());
		    if (bibliographicCitationDBLP != null) {
			Cluster cluster = LeeGilesDataHelper.extractCluster(dataFile, tokens[0]);
			Cluster clusterDB = clusterService.retrieveByKey(cluster.getKey());
			if (clusterDB == null) {
			    clusterService.create(cluster);

			} else {
			    cluster = clusterDB;
			}
			
			BibliographicCitationSynchronized bibliographicCitationSynchronized = BeansHelper.map(bibliographicCitationDBLP);
			bibliographicCitationSynchronized.setActualClass(cluster.getKey());
			bibliographicCitationSynchronizedService.create(bibliographicCitationSynchronized);
		    }
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

    /**
     * Generate txt file.
     */
    private void generateTxtFile() {
	Collection<BibliographicCitationSynchronized> bibliographicCitationsSynchronized = null;

	try {

	    // Header
	    Collection<String> columnNamesBibliographicCitation = FileUtils.extractFieldNames(BibliographicCitationSynchronized.class.newInstance());
	    Collection<String> columnNamesCluster = FileUtils.extractFieldNames(Cluster.class.newInstance());
	    FileUtils.writeHeaderToTxtFile(columnNamesBibliographicCitation, bibliographicCitationsFile, BatchLeeGilesData.SEPARATOR);
	    FileUtils.writeHeaderToTxtFile(columnNamesCluster, clustersFile, BatchLeeGilesData.SEPARATOR);
		
	    // Body
	    Collection<Cluster> clusters = clusterService.listAll();
	    for (Cluster cluster : clusters) {
		bibliographicCitationsSynchronized = bibliographicCitationSynchronizedService.listByCluster(cluster.getKey());
		FileUtils.writeToTxtFile(columnNamesBibliographicCitation, bibliographicCitationsSynchronized, bibliographicCitationsFile, BatchLeeGilesData.SEPARATOR);
		FileUtils.writeToTxtFile(columnNamesCluster, cluster, clustersFile, BatchLeeGilesData.SEPARATOR);
		LOG.info("generateTxtFile :: " + cluster.getKey());
	    }

	} catch (DLBusinessServiceException e) {
	    LOG.error(e.getMessage());

	} catch (DLIOException e) {
	    LOG.error(e.getMessage());
	    
	} catch (InstantiationException e) {
	    LOG.error(e.getMessage());
	    
	} catch (IllegalAccessException e) {
	    LOG.error(e.getMessage());
	}
    }
}