package com.wechat.music.provider.qq;

import com.wechat.music.model.BaseBean;
import com.wechat.music.model.MusicLink;

/**
 * Created by haohua on 2018/2/9.
 */
class QQSongLink extends BaseBean implements MusicLink {
    public String url;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public String getSongId() {
        return null;
    }

    @Override
    public long getBitRate() {
        return 0;
    }

    @Override
    public String getMd5() {
        return null;
    }
}
