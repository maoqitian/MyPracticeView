package com.starschina.sdk.demo.common.Epg;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface IEpgView {
    void showEpg(List<EpgEntity> epgs);
    void showErrorHint(String message);
}
