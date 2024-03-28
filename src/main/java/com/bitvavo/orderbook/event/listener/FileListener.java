package com.bitvavo.orderbook.event.listener;


import com.bitvavo.orderbook.event.model.FileEvent;

import java.util.EventListener;

public interface FileListener extends EventListener {
     void onCreated(FileEvent event);
}

