package com.wechat.music.provider.baidu;

import com.wechat.music.model.BaseBean;
import com.wechat.music.model.Lyric;

@SuppressWarnings("SpellCheckingInspection")
class BaiduLyric extends BaseBean implements Lyric {
    public String lyricUrl;

    @Override
    public String getLyric() {
        return null;
    }

    @Override
    public String getLyricUrl() {
        return lyricUrl;
    }
}
