/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.util.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.cgomez.model.author.Cluster;
import com.cgomez.util.constant.Constant;

/**
 * The Class LeeGilesDataHelper.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
public final class LeeGilesDataHelper {

    /**
     * Instantiates a new lee giles data helper.
     */
    private LeeGilesDataHelper() {
    }

    /**
     * Extract cluster.
     * 
     * @param dataFile
     *            the data file
     * @param str
     *            the str
     * @return the cluster
     */
    public static Cluster extractCluster(String dataFile, String str) {
	String[] tokens = str.split(Constant.SEMICOLON);
	String[] firstToken = tokens[0].split(Constant.WHITE_SPACE);
	Cluster cluster = new Cluster();
	cluster.setKey(FilenameUtils.getBaseName(dataFile).trim() + "_" + Integer.valueOf(firstToken[0].split(Constant.UNDER_LINE)[0]));
	return cluster;
    }

    /**
     * Extract authors.
     * 
     * @param str
     *            the str
     * @return the list
     */
    public static List<String> extractAuthors(String str) {
	List<String> authors = new ArrayList<String>();
	String[] tokens = str.split(Constant.SEMICOLON);
	String[] firstToken = tokens[0].split(Constant.WHITE_SPACE);

	String[] firstAuthor = Arrays.copyOfRange(firstToken, 1, firstToken.length);
	String name = Constant.EMPTY;
	for (String el : firstAuthor) {
	    name = name.concat(el).concat(Constant.WHITE_SPACE);
	}

	authors.add(name.trim());

	for (int i = 1; i < tokens.length; i++) {
	    if (tokens[i].trim().length() > 1) {
		authors.add(tokens[i].trim());
	    }
	}

	return authors;
    }
}