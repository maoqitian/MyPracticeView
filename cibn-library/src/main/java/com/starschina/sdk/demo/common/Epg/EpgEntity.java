package com.starschina.sdk.demo.common.Epg;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/10/14.
 */
public class EpgEntity {

    /**
     * epgId : 133929424
     * epgName : 午夜体育报道-体育咖吧
     * showId : 0
     * showName : 午夜体育报道
     * startDate : 16/10/14
     * weekDay : 星期五
     * startTime : 1476374400
     * endTime : 1476376200
     * duration : 1800
     * typeName : 体育竞技
     * label : 新闻
     * videoId : 59
     */

    private int epgId;
    private String epgName;
    private int showId;
    private String showName;
    private String startDate;
    private String weekDay;
    private int startTime;
    private int endTime;
    private int duration;
    private String typeName;
    private String label;
    private int videoId;

    private boolean isSelected;
    // 0回看 1当前直播
    private int status = -1;

    public int getEpgId() {
        return epgId;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public String getEpgName() {
        return epgName;
    }

    public void setEpgName(String epgName) {
        this.epgName = epgName;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getStatus() { return status;}

    public void setStatus(int s) {
        status = s;
    }

    public static EpgEntity parse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, EpgEntity.class);
    }
}
