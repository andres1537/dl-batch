/*
 * Copyright (c) 2015 cgomez. All rights reserved.
 */
package com.cgomez.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Component;

import com.cgomez.exception.DLBusinessServiceException;
import com.cgomez.model.bibliography.BibliographicCitationDBLP;
import com.cgomez.util.BeanUtils;
import com.cgomez.util.constant.BibliographicCitationEnum;
import com.cgomez.xml.bibliographic.Article;
import com.cgomez.xml.bibliographic.Book;
import com.cgomez.xml.bibliographic.Incollection;
import com.cgomez.xml.bibliographic.Inproceedings;
import com.cgomez.xml.bibliographic.Mastersthesis;
import com.cgomez.xml.bibliographic.Phdthesis;
import com.cgomez.xml.bibliographic.Proceedings;
import com.cgomez.xml.bibliographic.Www;

/**
 * dblpJava. 1. XML objects were generated from the dtd file via this command:
 * "xjc -dtd -d examples -p com.cgomez.xml dblp.dtd". 2. This is the main class
 * for running the process of extraction and storing of the records from
 * dblp.xml to a mongodb database. 3. It's necessary to run firstly the indexes
 * for the bibliographic citations collection (table). These can found in the
 * folder called 'scripts' of this same project. 4. I recommend you use the
 * following VM arguments to run this class: '-mx1500M
 * -DentityExpansionLimit=2500000' 5. This class can process a huge dblp.xml. I
 * was using a file > 1.6 Gb with more than 3 million bibliographic citations.
 * 
 * @author <a href="mailto:andres1537@gmail.com">Carlos Gomez</a>
 * @since dl-batch-1.0
 */
@Component
public final class BatchDBLP extends AbstractBatch {

    /** The article unmarshaller. */
    private Unmarshaller articleUnmarshaller;

    /** The inproceedings unmarshaller. */
    private Unmarshaller inproceedingsUnmarshaller;

    /** The proceedings unmarshaller. */
    private Unmarshaller proceedingsUnmarshaller;

    /** The book unmarshaller. */
    private Unmarshaller bookUnmarshaller;

    /** The incollection unmarshaller. */
    private Unmarshaller incollectionUnmarshaller;

    /** The phdthesis unmarshaller. */
    private Unmarshaller phdthesisUnmarshaller;

    /** The mastersthesis unmarshaller. */
    private Unmarshaller mastersthesisUnmarshaller;

    /** The www unmarshaller. */
    private Unmarshaller wwwUnmarshaller;

    /** The xml reader. */
    private XMLStreamReader xmlReader;

    /**
     * Insert.
     *
     * @param <T> the generic type
     * @param bibliographicCitationJAXB the bibliographic citation jaxb
     * @throws ParseException the parse exception
     * @throws DLBusinessServiceException the DL business service exception
     */
    private <T> void insert(JAXBElement<T> bibliographicCitationJAXB) throws ParseException, DLBusinessServiceException {
	BibliographicCitationDBLP bibliographicCitationDBLP = BeanUtils.copy(bibliographicCitationJAXB);
	bibliographicCitationDBLPService.create(bibliographicCitationDBLP);
    }

    /**
     * Parses the xml.
     *
     * @param xmlEventType the xml event type
     * @throws DLBusinessServiceException the DL business service exception
     * @throws ParseException the parse exception
     * @throws JAXBException the JAXB exception
     */
    private void parseXML(int xmlEventType) throws DLBusinessServiceException, ParseException, JAXBException {
	if (xmlEventType == XMLStreamConstants.START_ELEMENT) {
	    BibliographicCitationEnum type = BibliographicCitationEnum.getEnum(xmlReader.getName().toString());
	    switch (type) {
	    case ARTICLE:
		insert(articleUnmarshaller.unmarshal(xmlReader, Article.class));
		break;

	    case INPROCEEDINGS:
		insert(inproceedingsUnmarshaller.unmarshal(xmlReader, Inproceedings.class));
		break;

	    case PROCEEDINGS:
		insert(proceedingsUnmarshaller.unmarshal(xmlReader, Proceedings.class));
		break;

	    case BOOK:
		insert(bookUnmarshaller.unmarshal(xmlReader, Book.class));
		break;

	    case INCOLLECTION:
		insert(incollectionUnmarshaller.unmarshal(xmlReader, Incollection.class));
		break;

	    case PHDTHESIS:
		insert(phdthesisUnmarshaller.unmarshal(xmlReader, Phdthesis.class));
		break;

	    case MASTERSTHESIS:
		insert(mastersthesisUnmarshaller.unmarshal(xmlReader, Mastersthesis.class));
		break;

	    case WWW:
		insert(wwwUnmarshaller.unmarshal(xmlReader, Www.class));
		break;

	    default:
		break;
	    }
	}
    }

    /**
     * Execute.
     *
     * @param dataFile the data file
     */
    public void execute(String dataFile) {
	try {

	    articleUnmarshaller = JAXBContext.newInstance(Article.class).createUnmarshaller();
	    inproceedingsUnmarshaller = JAXBContext.newInstance(Inproceedings.class).createUnmarshaller();
	    proceedingsUnmarshaller = JAXBContext.newInstance(Proceedings.class).createUnmarshaller();
	    bookUnmarshaller = JAXBContext.newInstance(Book.class).createUnmarshaller();
	    incollectionUnmarshaller = JAXBContext.newInstance(Incollection.class).createUnmarshaller();
	    phdthesisUnmarshaller = JAXBContext.newInstance(Phdthesis.class).createUnmarshaller();
	    mastersthesisUnmarshaller = JAXBContext.newInstance(Mastersthesis.class).createUnmarshaller();
	    wwwUnmarshaller = JAXBContext.newInstance(Www.class).createUnmarshaller();

	    File file = new File(dataFile);
	    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	    xmlReader = inputFactory.createXMLStreamReader(new FileReader(file));
	    xmlReader.next();
	    xmlReader.nextTag();
	    xmlReader.require(XMLStreamConstants.START_ELEMENT, null, "dblp");
	    while (xmlReader.hasNext()) {
		parseXML(xmlReader.next());
	    }

	    xmlReader.close();

	} catch (FileNotFoundException e) {
	    LOG.error(e.getMessage());

	} catch (XMLStreamException e) {
	    LOG.error(e.getMessage());

	} catch (JAXBException e) {
	    LOG.error(e.getMessage());

	} catch (ParseException e) {
	    LOG.error(e.getMessage());

	} catch (DLBusinessServiceException e) {
	    LOG.error(e.getMessage());
	}
    }
}