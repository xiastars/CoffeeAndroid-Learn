package com.summer.demo.module.album.listener;

import android.widget.ImageView;
import android.widget.ToggleButton;

public interface OnItemClickListener {
		void onItemClick(ToggleButton toggleButton, int position, boolean isChecked, ImageView ivSelect);
	}