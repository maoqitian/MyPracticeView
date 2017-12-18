package com.starschina.sdk.demo.common.Epg;

import rx.Observable;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface IEpgDataSource {
    Observable<String> getCurrentEpg(String videoId);
    Observable<EpgListEntity> getEpgList(String videoId, int index);
}
