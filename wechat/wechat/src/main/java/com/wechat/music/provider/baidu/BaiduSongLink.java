package com.wechat.music.provider.baidu;

import com.wechat.music.model.BaseBean;
import com.wechat.music.model.MusicLink;

@SuppressWarnings("SpellCheckingInspection")
class BaiduSongLink extends BaseBean implements MusicLink {
    public String songId;
    public String url;
    public String format;
    public long size;
    public long bitRate;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return format;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getSongId() {
        return String.valueOf(songId);
    }

    @Override
    public long getBitRate() {
        return bitRate;
    }

    @Override
    public String getMd5() {
        // TODO
        return null;
    }
}
