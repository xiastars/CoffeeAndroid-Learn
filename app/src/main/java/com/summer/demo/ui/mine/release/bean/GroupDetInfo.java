package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class GroupDetInfo implements Serializable {

    String id;
    int type;
    String name;
    String sn;
    boolean is_default;
    String description;
    int cost;
    long create_time;
    int valid_month;
    String cover_url;
    TopicImgInfo background;
    private GroupUserInfo owner;
    private GroupCategoryInfo category;
    private PolicyInfo policies;
    private GroupStatisticInfo statistics;
    private GroupPublishInfo publish;
    private GroupUserInfo current_user;
    int group_level;//1:试运营，2正式
    int group_status;//星球状态 1：正常，2：申请中
    int limit_num;//限制加入最大数量
    int friend_num;//好友关注数据
    boolean isFake;

    public boolean isFake() {
        return isFake;
    }

    public void setFake(boolean fake) {
        isFake = fake;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public int getFriend_num() {
        return friend_num;
    }

    public void setFriend_num(int friend_num) {
        this.friend_num = friend_num;
    }

    public int getGroup_level() {
        return group_level;
    }

    public void setGroup_level(int group_level) {
        this.group_level = group_level;
    }

    public int getGroup_status() {
        return group_status;
    }

    public void setGroup_status(int group_status) {
        this.group_status = group_status;
    }

    public int getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(int limit_num) {
        this.limit_num = limit_num;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getValid_month() {
        return valid_month;
    }

    public void setValid_month(int valid_month) {
        this.valid_month = valid_month;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public TopicImgInfo getBackground() {
        if(background == null){
            return new TopicImgInfo();
        }
        return background;
    }

    public void setBackground(TopicImgInfo background) {
        this.background = background;
    }

    public GroupUserInfo getOwner() {
        if(owner == null){
            return new GroupUserInfo();
        }
        return owner;
    }

    public void setOwner(GroupUserInfo owner) {
        this.owner = owner;
    }

    public GroupCategoryInfo getCategory() {
        return category;
    }

    public void setCategory(GroupCategoryInfo category) {
        this.category = category;
    }

    public PolicyInfo getPolicies() {
        if(policies == null){
            return new PolicyInfo();
        }
        return policies;
    }

    public void setPolicies(PolicyInfo policies) {
        this.policies = policies;
    }

    public GroupStatisticInfo getStatistics() {
        if(statistics == null){
            return new GroupStatisticInfo();
        }
        return statistics;
    }

    public void setStatistics(GroupStatisticInfo statistics) {
        this.statistics = statistics;
    }

    public GroupPublishInfo getPublish() {
        return publish;
    }

    public void setPublish(GroupPublishInfo publish) {
        this.publish = publish;
    }

    public GroupUserInfo getCurrent_user() {
        if(current_user == null){
            return new GroupUserInfo();
        }
        return current_user;
    }

    public void setCurrent_user(GroupUserInfo current_user) {
        this.current_user = current_user;
    }
}

