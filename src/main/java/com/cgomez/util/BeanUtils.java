/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.util;

import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import com.cgomez.model.bibliography.BibliographicCitationDBLP;
import com.cgomez.util.constant.BibliographicCitationEnum;
import com.cgomez.xml.AbstractBibliographicCitation;
import com.cgomez.xml.attributes.Address;
import com.cgomez.xml.attributes.Author;
import com.cgomez.xml.attributes.Booktitle;
import com.cgomez.xml.attributes.Cdrom;
import com.cgomez.xml.attributes.Chapter;
import com.cgomez.xml.attributes.Cite;
import com.cgomez.xml.attributes.Crossref;
import com.cgomez.xml.attributes.Editor;
import com.cgomez.xml.attributes.Ee;
import com.cgomez.xml.attributes.Isbn;
import com.cgomez.xml.attributes.Journal;
import com.cgomez.xml.attributes.Month;
import com.cgomez.xml.attributes.Note;
import com.cgomez.xml.attributes.Number;
import com.cgomez.xml.attributes.Pages;
import com.cgomez.xml.attributes.Publisher;
import com.cgomez.xml.attributes.School;
import com.cgomez.xml.attributes.Series;
import com.cgomez.xml.attributes.Title;
import com.cgomez.xml.attributes.Url;
import com.cgomez.xml.attributes.Volume;
import com.cgomez.xml.attributes.Year;
import com.cgomez.xml.bibliographic.Article;
import com.cgomez.xml.bibliographic.Book;
import com.cgomez.xml.bibliographic.Incollection;
import com.cgomez.xml.bibliographic.Inproceedings;
import com.cgomez.xml.bibliographic.Mastersthesis;
import com.cgomez.xml.bibliographic.Phdthesis;
import com.cgomez.xml.bibliographic.Proceedings;
import com.cgomez.xml.bibliographic.Www;

/**
 * BeanUtils.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
public final class BeanUtils {

    /**
     * Instantiates a new bean utils.
     */
    private BeanUtils() {
    }

    /**
     * Copy.
     *
     * @param <T> the generic type
     * @param bibliographicCitationJAXB the bibliographic citation jaxb
     * @return the bibliographic citation dblp
     * @throws ParseException the parse exception
     */
    public static <T> BibliographicCitationDBLP copy(JAXBElement<T> bibliographicCitationJAXB) throws ParseException {
	if (Article.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Article) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.ARTICLE);

	} else if (Inproceedings.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Inproceedings) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.INPROCEEDINGS);

	} else if (Proceedings.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Proceedings) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.PROCEEDINGS);

	} else if (Incollection.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Incollection) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.INCOLLECTION);

	} else if (Book.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Book) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.BOOK);

	} else if (Phdthesis.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Phdthesis) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.PHDTHESIS);

	} else if (Mastersthesis.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Mastersthesis) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.MASTERSTHESIS);

	} else if (Www.class.isInstance(bibliographicCitationJAXB.getValue())) {
	    return mapBibliographicCitation((Www) bibliographicCitationJAXB.getValue(), BibliographicCitationEnum.WWW);
	}

	return null;
    }

    /**
     * Map bibliographic citation.
     *
     * @param bibliographicCitationXML the bibliographic citation xml
     * @param type the type
     * @return the bibliographic citation dblp
     * @throws ParseException the parse exception
     */
    private static BibliographicCitationDBLP mapBibliographicCitation(AbstractBibliographicCitation bibliographicCitationXML,
	    BibliographicCitationEnum type) throws ParseException {
	BibliographicCitationDBLP bibliographicCitationDBLP = new BibliographicCitationDBLP();
	bibliographicCitationDBLP.setAuthors(new ArrayList<String>());
	bibliographicCitationDBLP.setCitations(new ArrayList<String>());
	bibliographicCitationDBLP.setType(type);
	bibliographicCitationDBLP.setKey(bibliographicCitationXML.getKey());
	bibliographicCitationDBLP.setMdate(bibliographicCitationXML.getMdate());
	bibliographicCitationDBLP.setPubltype(bibliographicCitationXML.getPubltype());
	bibliographicCitationDBLP.setReviewid(bibliographicCitationXML.getReviewid());
	bibliographicCitationDBLP.setRating(bibliographicCitationXML.getRating());

	for (Object object : bibliographicCitationXML
		.getAuthorOrEditorOrTitleOrBooktitleOrPagesOrYearOrAddressOrJournalOrVolumeOrNumberOrMonthOrUrlOrEeOrCdromOrCiteOrPublisherOrNoteOrCrossrefOrIsbnOrSeriesOrSchoolOrChapter()) {
	    if (Author.class.isInstance(object)) {
		bibliographicCitationDBLP.getAuthors().add(((Author) object).getvalue());

	    } else if (Editor.class.isInstance(object)) {
		bibliographicCitationDBLP.setEditor(((Editor) object).getvalue());

	    } else if (Title.class.isInstance(object)) {
		bibliographicCitationDBLP.setTitle(((Title) object).getvalue());

	    } else if (Booktitle.class.isInstance(object)) {
		bibliographicCitationDBLP.setBooktitle(((Booktitle) object).getvalue());

	    } else if (Pages.class.isInstance(object)) {
		bibliographicCitationDBLP.setPages(((Pages) object).getvalue());

	    } else if (Year.class.isInstance(object)) {
		bibliographicCitationDBLP.setYear(Integer.valueOf(((Year) object).getvalue()));

	    } else if (Address.class.isInstance(object)) {
		bibliographicCitationDBLP.setAddress(((Address) object).getvalue());

	    } else if (Journal.class.isInstance(object)) {
		bibliographicCitationDBLP.setJournal(((Journal) object).getvalue());

	    } else if (Volume.class.isInstance(object)) {
		bibliographicCitationDBLP.setVolume(((Volume) object).getvalue());

	    } else if (Number.class.isInstance(object)) {
		bibliographicCitationDBLP.setNumber(((Number) object).getvalue());

	    } else if (Month.class.isInstance(object)) {
		bibliographicCitationDBLP.setMonth(((Month) object).getvalue());

	    } else if (Url.class.isInstance(object)) {
		bibliographicCitationDBLP.setUrl(((Url) object).getvalue());

	    } else if (Ee.class.isInstance(object)) {
		bibliographicCitationDBLP.setEe(((Ee) object).getvalue());

	    } else if (Cdrom.class.isInstance(object)) {
		bibliographicCitationDBLP.setCdrom(((Cdrom) object).getvalue());

	    } else if (Cite.class.isInstance(object)) {
		bibliographicCitationDBLP.getCitations().add(((Cite) object).getvalue());

	    } else if (Publisher.class.isInstance(object)) {
		bibliographicCitationDBLP.setPublisher(((Publisher) object).getvalue());

	    } else if (Note.class.isInstance(object)) {
		bibliographicCitationDBLP.setNote(((Note) object).getvalue());

	    } else if (Crossref.class.isInstance(object)) {
		bibliographicCitationDBLP.setCrossref(((Crossref) object).getvalue());

	    } else if (Isbn.class.isInstance(object)) {
		bibliographicCitationDBLP.setIsbn(((Isbn) object).getvalue());

	    } else if (Series.class.isInstance(object)) {
		bibliographicCitationDBLP.setSeries(((Series) object).getvalue());

	    } else if (School.class.isInstance(object)) {
		bibliographicCitationDBLP.setSchool(((School) object).getvalue());

	    } else if (Chapter.class.isInstance(object)) {
		bibliographicCitationDBLP.setChapter(((Chapter) object).getvalue());
	    }
	}

	return bibliographicCitationDBLP;
    }
}