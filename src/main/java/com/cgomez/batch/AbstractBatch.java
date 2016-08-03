/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cgomez.service.IBibliographicCitationDBLPService;
import com.cgomez.service.IBibliographicCitationLeeGilesService;
import com.cgomez.service.IBibliographicCitationSynchronizedService;
import com.cgomez.service.IClusterService;

/**
 * The Class AbstractBatch.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
public abstract class AbstractBatch {

    /** The Constant LOG. */
    protected static final Logger LOG = Logger.getLogger(AbstractBatch.class);

    /** The bibliographic citation lee giles service. */
    @Autowired
    protected IBibliographicCitationLeeGilesService bibliographicCitationLeeGilesService;
    
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