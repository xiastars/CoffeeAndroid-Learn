package com.summer.demo.ui.module.comment;

import java.io.Serializable;

/**
 * Created by xiaqiliang on 2017/3/28.
 */
public class StarCommentInfo implements Serializable {

    String userHeadImg;
    long createdTime;
    long id;
    long userId;
    String content;
    String realname;
    String replayRealname;
    long replyUserId;
    int bePraiseCount;
    boolean bePraiseFlag;
    int betResult;//1红方 0蓝方
    int userBetValue;//下注积分
    long eventId;
    int level;//楼层
    int totalCount;
    private String hangingImg;

    public String getHangingImg() {
        return hangingImg;
    }

    public void setHangingImg(String hangingImg) {
        this.hangingImg = hangingImg;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getBetResult() {
        return betResult;
    }

    public void setBetResult(int betResult) {
        this.betResult = betResult;
    }

    public int getUserBetValue() {
        return userBetValue;
    }

    public void setUserBetValue(int userBetValue) {
        this.userBetValue = userBetValue;
    }

    public int getBePraiseCount() {
        return bePraiseCount;
    }

    public void setBePraiseCount(int bePraiseCount) {
        this.bePraiseCount = bePraiseCount;
    }

    public boolean isBePraiseFlag() {
        return bePraiseFlag;
    }

    public void setBePraiseFlag(boolean bePraiseFlag) {
        this.bePraiseFlag = bePraiseFlag;
    }

    public String getReplayRealname() {
        return replayRealname;
    }

    public void setReplayRealname(String replayRealname) {
        this.replayRealname = replayRealname;
    }

    public long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
