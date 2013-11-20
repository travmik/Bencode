package com.travmik.bittorrent.bencode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.travmik.bittorrent.type.DictionaryType;
import com.travmik.bittorrent.util.BenUtil;

public class BenFile {
    private final String announce;
    private final String createdBy;
    private final Integer creationDate;
    private final String comment;
    private final DictionaryType info;

    private BenFile(final DictionaryType root) {
        this.announce = (String) root.lookup(BenUtil.ANNOUNCE_KEY).getNativeValue();
        this.createdBy = (String) root.lookup(BenUtil.CREATED_BY_KEY).getNativeValue();
        this.creationDate = (Integer) root.lookup(BenUtil.CREATION_DATE_KEY).getNativeValue();
        this.comment = (String) root.lookup(BenUtil.COMMENT_KEY).getNativeValue();
        this.info = (DictionaryType) root.lookup(BenUtil.INFO_KEY);
    }

    public static BenFile open(final String path) throws IOException {
        return BenFile.open(new File(path));
    }

    public static BenFile open(final File file) throws IOException {
        final BenInputStream in = new BenInputStream(new FileInputStream(file));
        final DictionaryType root = (DictionaryType) in.readNextType();

        return new BenFile(root);
    }

    public String getAnnounce() {
        return announce;
    }

    public DictionaryType getInfo() {
        return info;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Integer getCreationDate() {
        return creationDate;
    }

    public String getComment() {
        return comment;
    }
}
