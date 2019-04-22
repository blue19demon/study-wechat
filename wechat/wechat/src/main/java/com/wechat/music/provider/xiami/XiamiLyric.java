package com.wechat.music.provider.xiami;

import com.wechat.music.model.BaseBean;
import com.wechat.music.model.Lyric;

@SuppressWarnings("SpellCheckingInspection")
class XiamiLyric extends BaseBean implements Lyric {
    private String lyricUrl;

    @Override
    public String getLyric() {
        return null;
    }

    @Override
    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }
}
