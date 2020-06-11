package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class GroupUserInfo implements Serializable {
    private String id;
    private String avatar;
    private String name;
    private int identity = -1;
    private int join_time;
    private boolean is_black;
    private int rank;
    boolean is_removed;
    boolean isFake;
    boolean is_active;
    MemberInfo member_info;
    boolean localChecked;
    boolean is_guest;//是否是嘉宾
    long pre_valid_time;
    long current_time;
    boolean is_member;//是不是星球成员
    boolean is_like;
    UserRelationInfo target_relation;
    String group_id;
    int invite_num;
    float reward;

    public float getReward() {
        return reward;
    }

    public void setReward(float reward) {
        this.reward = reward;
    }

    public int getInvite_num() {
        return invite_num;
    }

    public void setInvite_num(int invite_num) {
        this.invite_num = invite_num;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public UserRelationInfo getTarget_relation() {
        if(target_relation == null){
            return new UserRelationInfo();
        }
        return target_relation;
    }

    public void setTarget_relation(UserRelationInfo target_relation) {
        this.target_relation = target_relation;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public boolean isIs_member() {
        return is_member;
    }

    public void setIs_member(boolean is_member) {
        this.is_member = is_member;
    }

    public long getPre_valid_time() {
        return pre_valid_time;
    }

    public void setPre_valid_time(long pre_valid_time) {
        this.pre_valid_time = pre_valid_time;
    }

    public long getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(long current_time) {
        this.current_time = current_time;
    }

    public boolean isIs_guest() {
        return is_guest;
    }

    public void setIs_guest(boolean is_guest) {
        this.is_guest = is_guest;
    }

    public boolean isLocalChecked() {
        return localChecked;
    }

    public void setLocalChecked(boolean localChecked) {
        this.localChecked = localChecked;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean isIs_removed() {
        return is_removed;
    }

    public void setIs_removed(boolean is_removed) {
        this.is_removed = is_removed;
    }

    public MemberInfo getMember_info() {
        if(member_info == null){
            return new MemberInfo();
        }
        return member_info;
    }

    public void setMember_info(MemberInfo member_info) {
        this.member_info = member_info;
    }

    public boolean isFake() {
        return isFake;
    }

    public void setFake(boolean fake) {
        isFake = fake;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getJoin_time() {
        return join_time;
    }

    public void setJoin_time(int join_time) {
        this.join_time = join_time;
    }

    public boolean isIs_black() {
        return is_black;
    }

    public void setIs_black(boolean is_black) {
        this.is_black = is_black;
    }
}
