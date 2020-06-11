package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class PolicyInfo implements Serializable {
    private int allow_sharing;
    private int allow_paying_for_topic;
    private int allow_private_chat;
    private int need_examine;
    private int allow_search;
    private int allow_join;
    private int allow_anonymous_question;
    private int silence_new_member;
    private int create_topic;
    private int members_visibility;
    private String join_notice;
    private String user_introduction;
    private int create_topic_in_day;
    private int forbid_share;
    public void setAllow_sharing(int allow_sharing) {
        this.allow_sharing = allow_sharing;
    }
    public int getAllow_sharing() {
        return allow_sharing;
    }
    String join_question;//审核介绍
    int examine_type;//0：不验证；1：信息验证；2：图片，3：回答问题
    int examine_status;//审批状态，0，用户未发起，1，已支付，2，等待提交 审核超时，3:等待审核，4，等待提交 审核超时，5，已通过
    int allow_tourist_visit;//1.2.7 允许星球外人查看

    public int getAllow_tourist_visit() {
        return allow_tourist_visit;
    }

    public void setAllow_tourist_visit(int allow_tourist_visit) {
        this.allow_tourist_visit = allow_tourist_visit;
    }

    public int getExamine_status() {
        return examine_status;
    }

    public void setExamine_status(int examine_status) {
        this.examine_status = examine_status;
    }

    public String getJoin_question() {
        return join_question;
    }

    public void setJoin_question(String join_question) {
        this.join_question = join_question;
    }

    public int getExamine_type() {
        return examine_type;
    }

    public void setExamine_type(int examine_type) {
        this.examine_type = examine_type;
    }

    public void setAllow_paying_for_topic(int allow_paying_for_topic) {
        this.allow_paying_for_topic = allow_paying_for_topic;
    }
    public int getAllow_paying_for_topic() {
        return allow_paying_for_topic;
    }

    public void setAllow_private_chat(int allow_private_chat) {
        this.allow_private_chat = allow_private_chat;
    }
    public int getAllow_private_chat() {
        return allow_private_chat;
    }

    public void setNeed_examine(int need_examine) {
        this.need_examine = need_examine;
    }
    public int getNeed_examine() {
        return need_examine;
    }

    public void setAllow_search(int allow_search) {
        this.allow_search = allow_search;
    }
    public int getAllow_search() {
        return allow_search;
    }

    public void setAllow_join(int allow_join) {
        this.allow_join = allow_join;
    }
    public int getAllow_join() {
        return allow_join;
    }

    public void setAllow_anonymous_question(int allow_anonymous_question) {
        this.allow_anonymous_question = allow_anonymous_question;
    }
    public int getAllow_anonymous_question() {
        return allow_anonymous_question;
    }

    public void setSilence_new_member(int silence_new_member) {
        this.silence_new_member = silence_new_member;
    }
    public int getSilence_new_member() {
        return silence_new_member;
    }

    public void setCreate_topic(int create_topic) {
        this.create_topic = create_topic;
    }
    public int getCreate_topic() {
        return create_topic;
    }

    public void setMembers_visibility(int members_visibility) {
        this.members_visibility = members_visibility;
    }
    public int getMembers_visibility() {
        return members_visibility;
    }

    public void setJoin_notice(String join_notice) {
        this.join_notice = join_notice;
    }
    public String getJoin_notice() {
        return join_notice;
    }

    public void setUser_introduction(String user_introduction) {
        this.user_introduction = user_introduction;
    }
    public String getUser_introduction() {
        return user_introduction;
    }

    public void setCreate_topic_in_day(int create_topic_in_day) {
        this.create_topic_in_day = create_topic_in_day;
    }
    public int getCreate_topic_in_day() {
        return create_topic_in_day;
    }

    public void setForbid_share(int forbid_share) {
        this.forbid_share = forbid_share;
    }
    public int getForbid_share() {
        return forbid_share;
    }
}
