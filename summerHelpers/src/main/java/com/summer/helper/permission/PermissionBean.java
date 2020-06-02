package com.summer.helper.permission;

import android.widget.Switch;

class PermissionBean {
        String name;
        Switch swView;
        boolean checked;

        public Switch getSwView() {
            return swView;
        }

        public void setSwView(Switch swView) {
            this.swView = swView;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }