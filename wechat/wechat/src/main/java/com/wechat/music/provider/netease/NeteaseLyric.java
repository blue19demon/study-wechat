package com.wechat.music.provider.netease;

import com.alibaba.fastjson.annotation.JSONField;
import com.wechat.music.model.BaseBean;
import com.wechat.music.model.Lyric;

@SuppressWarnings("SpellCheckingInspection")
public
class NeteaseLyric extends BaseBean implements Lyric {
    @JSONField(name = "lyric")
    public String lyric;

    @Override
    public String getLyric() {
        return lyric;
    }

    @Override
    public String getLyricUrl() {
        return null;
    }
}
