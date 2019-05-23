package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class NeteaseMusicTest extends MusicTestClassByProvider {
    public NeteaseMusicTest() {
        super(MusicProvider.Netease);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        final String query = "Suede";
        builder.iCanSearchMusicPleaseTestMeWithQuery(query);
        builder.iCanGetMusicLinkPleaseTestMeWithQuery(query);
        builder.iCanGetAlbumInfoPleaseTestMeWithQuery(query);
        builder.iCanGetMusicDetailPleaseTestMeWithQuery(query);
    }
}
