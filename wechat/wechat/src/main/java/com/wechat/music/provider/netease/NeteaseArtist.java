package com.wechat.music.provider.netease;

import com.alibaba.fastjson.annotation.JSONField;
import com.wechat.music.model.Artist;
import com.wechat.music.model.BaseBean;

/**
 * Created by haohua on 2018/2/12.
 */

@SuppressWarnings("SpellCheckingInspection")
class NeteaseArtist extends BaseBean implements Artist {
    @JSONField(name = "name")
    public String name;

    @JSONField(name = "id")
    public long id;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }
}
