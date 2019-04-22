package com.wechat.music.provider.migu;

import com.wechat.music.model.BaseBean;
import com.wechat.music.model.MusicLink;

@SuppressWarnings("SpellCheckingInspection")
class MiguSongLink extends BaseBean implements MusicLink {

    private String url;

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

    public void setUrl(String url) {
        this.url = url;
    }
}
