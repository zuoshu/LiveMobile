package com.oneguy.live.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ZuoShu on 6/28/16.
 */
public class Room {
    @JSONField(name = "room_id")
    private String roomId;

    @JSONField(name = "thumbnail")
    private String thumbnail;

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "url")
    private String url;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
