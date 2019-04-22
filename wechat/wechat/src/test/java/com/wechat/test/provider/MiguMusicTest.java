package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class MiguMusicTest extends MusicTestClassByProvider {
    public MiguMusicTest() {
        super(MusicProvider.Migu);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        builder.iCanSearchMusicPleaseTestMeWithQuery("孙燕姿");
    }
}
