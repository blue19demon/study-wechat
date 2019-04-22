package com.wechat.music.provider.xiami;

import com.alibaba.fastjson.annotation.JSONField;
import com.wechat.music.model.Artist;
import com.wechat.music.model.BaseBean;

@SuppressWarnings("SpellCheckingInspection")
class XiamiSinger extends BaseBean implements Artist {
    @JSONField(name = "artist_name")
    public String artistName;

    @JSONField(name = "artist_id")
    public String artistId;

    @Override
    public String getName() {
        return artistName;
    }

    @Override
    public String getId() {
        return artistId;
    }
}
