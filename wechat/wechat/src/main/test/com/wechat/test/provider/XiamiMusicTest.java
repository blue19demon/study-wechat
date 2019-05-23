package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class XiamiMusicTest extends MusicTestClassByProvider {
    public XiamiMusicTest() {
        super(MusicProvider.Xiami);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        String query = "五环之歌";
        builder.iCanSearchMusicPleaseTestMeWithQuery(query);
        builder.iCanGetAlbumInfoPleaseTestMeWithQuery(query);
    }
}
