package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class QQMusicTest extends MusicTestClassByProvider {
    public QQMusicTest() {
        super(MusicProvider.QQ);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        String query = "孙燕姿";
        builder.iCanSearchMusicPleaseTestMeWithQuery(query);
        builder.iCanGetAlbumInfoPleaseTestMeWithQuery(query);
    }
}
