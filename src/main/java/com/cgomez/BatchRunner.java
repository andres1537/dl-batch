/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cgomez.batch.BatchDBLP;
import com.cgomez.batch.BatchLeeGilesData;
import com.cgomez.batch.BatchSync;
import com.cgomez.extractors.ExtractorTitlesPerVenue;
import com.cgomez.util.constant.Constant;

/**
 * The Class BatchRunner.
 *
 * @author <a href="mailto:andres1537@gmail.com">Carlos A. Gómez</a>
 * @since dl-batch-1.0
 */
public final class BatchRunner {
    
    /** The Constant LOG. */
    protected static final Logger LOG = Logger.getLogger(BatchRunner.class);
    
    /** The context. */
    protected static ApplicationContext context;
    
    /** The arguments. */
    protected static String[] arguments;
    
    /** The name. */
    protected static String name;
    
    /** The data files. */
    protected static String[] dataFiles;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
	parseArguments(args);
	LOG.info("-----------------------------------------");
	LOG.info(name);
	LOG.info("-----------------------------------------");
	long start = System.currentTimeMillis();

	switch (name) {
	case "BatchLeeGilesData":
	    context = new ClassPathXmlApplicationContext("clgilesdata/clgiles-data-context.xml");
	    context.getBean(BatchLeeGilesData.class).execute(dataFiles);
	    break;

	case "BatchDBLP":
	    context = new ClassPathXmlApplicationContext("dblp/dblp-context.xml");
	    context.getBean(BatchDBLP.class).execute(dataFiles[0]);
	    break;

	case "BatchSync":
	    context = new ClassPathXmlApplicationContext("sync/sync-context.xml");
	    context.getBean(BatchSync.class).execute(dataFiles);
	    break;
	    
	case "ExtractorTitlesPerVenue":
	    context = new ClassPathXmlApplicationContext("titlespervenue/titlespervenue-context.xml");
	    context.getBean(ExtractorTitlesPerVenue.class).extractJournal();
	    context.getBean(ExtractorTitlesPerVenue.class).extractBookTitle();
	    break;

	default:
	    LOG.error("-n <name> -f <files>" + name);
	    break;
	}

	((ConfigurableApplicationContext) context).close();
	long end = System.currentTimeMillis();
	LOG.info("Time taken in segs: " + (double) (end - start) / 1000);
    }
    
    /**
     * Gets the value.
     *
     * @param option the option
     * @param defaultValue the default value
     * @return the value
     */
    private static String getValue(String option, String defaultValue) {
	int index = -1;
	final String value;
	for (int i = 0; i < arguments.length; i++) {
	    if (arguments[i].equalsIgnoreCase(option)) {
		index = i;
	    }
	}

	if (index >= 0) {
	    value = arguments[index + 1];

	} else {
	    value = defaultValue;
	}

	return value;
    }
    
    /**
     * Parses the arguments.
     *
     * @param args the args
     */
    private static void parseArguments(String[] args) {
	arguments = args;
	name = getValue("-n", "");
	dataFiles = getValue("-f", "").split(Constant.COLON);
    }
}