package com.wechat.music.provider.weibo;

import com.wechat.music.model.BaseBean;
import com.wechat.music.model.MusicLink;

@SuppressWarnings("SpellCheckingInspection")
class WeiboSongLink extends BaseBean implements MusicLink {
    public String url;

    public long songId;

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
        return String.valueOf(songId);
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
