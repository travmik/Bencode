package com.travmik.bittorent;


import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.travmik.bittorrent.bencode.BenFile;
import com.travmik.bittorrent.type.AbstractType;
import com.travmik.bittorrent.type.DictionaryType;
import com.travmik.bittorrent.type.DictionaryType.DictionaryElement;
import com.travmik.bittorrent.type.ListType;

import static junit.framework.Assert.assertEquals;

public class BTEncodeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BTEncodeTest.class);

    private static final String ANNOUNCE = "http://www.menulet.me";
    private static final String CREATED_BY = "travmik";
    private static final Integer CREATION_DATE = 1384498884;
    private static final String COMMENT = "b-encode implementation";

    private static final String FILES_KEY = "files";
    private static final String LENGTH_KEY = "length";
    private static final Integer LENGTH_VALUE = 35519736;
    private static final String PATH_KEY = "path";
    private static final String PATH_VALUE = "bencoder.jar";

    @Test
    public void shouldReadRealTorrentFile() throws Exception {

        final URL path = BTEncodeTest.class.getResource("/test.torrent");

        final File file = new File(path.toURI());
        final BenFile bFile = BenFile.open(file);
//        print(bFile);

        assertEquals("Invalid announce", ANNOUNCE, bFile.getAnnounce());
        assertEquals("Invalid created by", CREATED_BY, bFile.getCreatedBy());
        assertEquals("Invalid creation date", CREATION_DATE, bFile.getCreationDate());
        assertEquals("Invalid comment", COMMENT, bFile.getComment());

        DictionaryType files = (DictionaryType)((ListType)bFile.getInfo().lookup(FILES_KEY)).getNativeValue().get(0);
        assertEquals("Invalid length", LENGTH_VALUE, files.lookup(LENGTH_KEY).getNativeValue());
        assertEquals("Invalid path", PATH_VALUE, ((ListType)files.lookup(PATH_KEY)).getNativeValue().get(0).getNativeValue());

        assertEquals("Invalid name", "Bencoder 1.0", bFile.getInfo().lookup("name").getNativeValue());
        assertEquals("Invalid piece length", 131072, bFile.getInfo().lookup("piece length").getNativeValue());
        assertEquals("Invalid piece length", "#some_data#", bFile.getInfo().lookup("pieces").getNativeValue());
        assertEquals("Invalid piece length", 1, bFile.getInfo().lookup("private").getNativeValue());
    }

    private void print(final BenFile bFile) {
        LOGGER.info("Announce: {}", bFile.getAnnounce());
        LOGGER.info("Created by: {}", bFile.getCreatedBy());
        LOGGER.info("Creation date: {}", bFile.getCreationDate());
        LOGGER.info("Comment: {}", bFile.getComment());
        List<DictionaryElement> elements = bFile.getInfo().getNativeValue();
        recursivePrint(elements);
    }

    private void recursivePrint(final List<DictionaryElement> elements) {
        for(final DictionaryElement element : elements) {
            if(element.getValue() instanceof ListType) {
                final List<AbstractType> types = ((ListType) element.getValue()).getNativeValue();
                for(final AbstractType type : types) {
                    if(type instanceof DictionaryType) {
                        recursivePrint(((DictionaryType) type).getNativeValue());
                    } else {
                        printElement(element);
                    }
                }
            } else {
                printElement(element);
            }
        }
    }

    private void printElement(final DictionaryElement element) {
        LOGGER.info("{}: {}", new Object[]{element.getKey().getNativeValue(), element.getValue()
                .getNativeValue()});
    }
}
