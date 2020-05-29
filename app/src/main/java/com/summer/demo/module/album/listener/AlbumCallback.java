package com.summer.demo.module.album.listener;

import com.summer.demo.module.album.util.ImageItem;

import java.util.List;

public interface AlbumCallback {
        void doSelected(List<ImageItem> images);
    }