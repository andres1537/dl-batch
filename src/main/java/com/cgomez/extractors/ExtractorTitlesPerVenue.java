/*
 * Copyright (c) 2016 cgomez. All rights reserved.
 */
package com.cgomez.extractors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cgomez.exception.DLBusinessServiceException;
import com.cgomez.exception.DLIOException;
import com.cgomez.model.bibliography.BibliographicCitationDBLP;
import com.cgomez.model.field.BibliographicCitationSynchronizedField;
import com.cgomez.util.FileUtils;
import com.cgomez.util.StringUtils;
import com.cgomez.util.constant.Constant;
import com.cgomez.util.mongo.BibliographicCitationAggregationResult;

/**
 * The Class ExtractorTitlesPerVenue.
 *
 * @author <a href="mailto:andres1537@gmail.com">Carlos A. Gómez</a>
 * @since dl-batch-1.0
 */
@Component
public final class ExtractorTitlesPerVenue extends AbstractExtractor {
    
    /** The titles per journal file. */
    @Value("${titlesPerJournal.file}")
    private String titlesPerJournalFile;
    
    /** The titles per book title file. */
    @Value("${titlesPerBookTitle.file}")
    private String titlesPerBookTitleFile;
    
    /**
     * Extract journal.
     */
    public void extractJournal() {
	LOG.info("extractJournal --------------------------");
	extract(BibliographicCitationSynchronizedField.JOURNAL, titlesPerJournalFile);
    }
    
    /**
     * Extract book title.
     */
    public void extractBookTitle() {
	LOG.info("extractBookTitle --------------------------");
	extract(BibliographicCitationSynchronizedField.BOOK_TITLE, titlesPerBookTitleFile);
    }
    
    /**
     * Extract.
     *
     * @param field the field
     * @param file the file
     */
    private void extract(String field, String file) {
	List<BibliographicCitationAggregationResult> groups = null;
	Collection<BibliographicCitationDBLP> bibliographicCitations = null;
	StringBuffer titles = null;
	String title = null;
	
	try {
	    
	    // Header
	    Collection<String> columnNames = new ArrayList<String>();
	    columnNames.add(field);
	    columnNames.add("titles");
	    FileUtils.writeHeaderToTxtFile(columnNames, file, Constant.SEMICOLON);
	    groups = bibliographicCitationDBLPService.group(field);

	    for (BibliographicCitationAggregationResult group : groups) {
		bibliographicCitations = bibliographicCitationDBLPService.retrieve(field, group.get_id());
		if (StringUtils.isNoneBlank(group.get_id())) {
    		   if (bibliographicCitations.size() != group.getCount()) {
    		       throw new DLBusinessServiceException("Error: the count of bibliographic citations is different than the group.", null);
    		   }
		
    		   LOG.info(field + "::: " +group.get_id());
    		   titles = new StringBuffer(group.get_id());
    		   titles.append(Constant.SEMICOLON);
    		   for (BibliographicCitationDBLP bibliographicCitationDBLP : bibliographicCitations) {
    		       title = StringUtils.removeNonLetterCharacters(bibliographicCitationDBLP.getTitle());
    		       title = StringUtils.removeStopWords(title);
    		       titles.append(title);
    		       titles.append(Constant.WHITE_SPACE);
    		   }
		
    		   // Body
    		   FileUtils.writeToTxtFile(titles.toString(), file);
		}
	    }
	    	    
	} catch (DLBusinessServiceException e) {
	    LOG.error(e.getMessage());
	    
	} catch (DLIOException e) {
	    LOG.error(e.getMessage());
	}
    }
}