package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class GroupStatisticInfo implements Serializable{


        private int topics_count;
        private int answers_count;
        private int digests_count;
        private int member_num;
        public void setTopics_count(int topics_count) {
            this.topics_count = topics_count;
        }
        public int getTopics_count() {
            return topics_count;
        }

        public void setAnswers_count(int answers_count) {
            this.answers_count = answers_count;
        }
        public int getAnswers_count() {
            return answers_count;
        }

        public void setDigests_count(int digests_count) {
            this.digests_count = digests_count;
        }
        public int getDigests_count() {
            return digests_count;
        }

        public void setMember_num(int member_num) {
            this.member_num = member_num;
        }
        public int getMember_num() {
            return member_num;
        }

    }
