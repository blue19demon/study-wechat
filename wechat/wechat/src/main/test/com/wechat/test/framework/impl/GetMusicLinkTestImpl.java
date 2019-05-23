package com.wechat.test.framework.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;

import com.wechat.music.api.MusicApi;
import com.wechat.music.api.MusicApiFactory;
import com.wechat.music.api.MusicProvider;
import com.wechat.music.model.MusicLink;
import com.wechat.music.model.Song;
import com.wechat.test.framework.AbsMusicTestCase;
import com.wechat.test.framework.mark.GetMusicLinkTest;
import com.wechat.test.util.TestUtils;

@Ignore
public class GetMusicLinkTestImpl extends AbsMusicTestCase implements GetMusicLinkTest {

    private MusicProvider provider;
    private MusicApi api;
    private String searchQuery;

    @Override
    public void init(Object[] args) {
        provider = (MusicProvider) args[0];
        searchQuery = (String) args[1];
        api = MusicApiFactory.create(provider);
    }

    @Override
    public void runTest() throws Exception {
        List<? extends Song> songs = api.searchMusicSync(searchQuery, 0, false);
        String songId = songs.get(0).getSongId();
        MusicLink link = api.getMusicLinkByIdSync(songId);
        assertEquals(link.getSongId(), songId);
        String url = link.getUrl();
        println("url: " + url);
        assertEquals(200, TestUtils.testDownload(url));
    }
}
