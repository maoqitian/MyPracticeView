package com.starschina.sdk.demo.common;

import org.json.JSONObject;

import java.io.Serializable;

public class Channel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5387497563802039553L;
	public int videoId;
	public String videoName;
	public String videoImg;
	public String shareImgUrl;
	public String icon;
	
	public static Channel parseChannel(JSONObject jObjChannel){
		Channel ch = null;
		if(jObjChannel != null){
			ch = new Channel();
			ch.videoId = jObjChannel.optInt("videoId");
			ch.videoName = jObjChannel.optString("videoName");
			ch.videoImg = jObjChannel.optString("videoImage");
			ch.shareImgUrl = jObjChannel.optString("shareImage");
			ch.icon = jObjChannel.optString("videoImage");
		}
		return ch;
	}
}
