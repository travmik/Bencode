package com.travmik.bittorrent.type;

public class EmptyType extends AbstractType {

    public EmptyType() {
    }

    @Override
    public String getNativeValue() {
        return "";
    }

    @Override
    public String toString() {
        return "[EmptyType value=" + getNativeValue() + "]";
    }
}
