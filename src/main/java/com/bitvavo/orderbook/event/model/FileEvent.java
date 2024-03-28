package com.bitvavo.orderbook.event.model;

import java.util.EventObject;
import java.io.File;

public class FileEvent extends EventObject {
    public FileEvent(File file) {
        super(file);
    }

    public File getFile() {
        return (File) getSource();
    }

}

