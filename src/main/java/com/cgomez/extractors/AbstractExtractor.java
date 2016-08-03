/*
 * Copyright (c) 2016 cgomez. All rights reserved.
 */
package com.cgomez.extractors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cgomez.service.IBibliographicCitationDBLPService;
import com.cgomez.service.IBibliographicCitationSynchronizedService;
import com.cgomez.service.IClusterService;

/**
 * The Class AbstractExtractor.
 *
 * @author <a href="mailto:andres1537@gmail.com">Carlos A. Gómez</a>
 * @since dl-batch-1.0
 */
public abstract class AbstractExtractor {
    
    /** The Constant LOG. */
    protected static final Logger LOG = Logger.getLogger(AbstractExtractor.class);
    
    /** The bibliographic citation dblp service. */
    @Autowired
    protected IBibliographicCitationDBLPService bibliographicCitationDBLPService;
    
    /** The bibliographic citation synchronized service. */
    @Autowired
    protected IBibliographicCitationSynchronizedService bibliographicCitationSynchronizedService;
    
    /** The cluster service. */
    @Autowired
    protected IClusterService clusterService;
}