package com.wechat.test.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import com.wechat.music.api.MusicApi;
import com.wechat.music.api.MusicApiFactory;
import com.wechat.music.api.MusicProvider;
import com.wechat.music.model.Album;
import com.wechat.music.model.Song;
import com.wechat.music.util.TextUtils;
import com.wechat.test.framework.AbsMusicTestCase;
import com.wechat.test.framework.mark.GetAlbumTest;

public class GetAlbumTestImpl extends AbsMusicTestCase implements GetAlbumTest {
    private MusicApi api;
    private String searchQuery;
    private MusicProvider provider;

    @Override
    public void init(Object[] args) {
        provider = (MusicProvider) args[0];
        searchQuery = (String) args[1];
        api = MusicApiFactory.create(provider);
    }

    @Override
    public void runTest() throws IOException {
        List<? extends Song> page0 = api.searchMusicSync(searchQuery, 0, true);
        String albumId = null;
        for (Song song : page0) {
            albumId = song.getAlbum().getAlbumId();
            if (!TextUtils.isEmpty(albumId)) {
                break;
            }
        }
        Album album = api.getAlbumInfoByIdSync(albumId, false);
        println("album: " + album);
        assertNotNull(album.getName());
        assertEquals(albumId, album.getAlbumId());
        assertTrue(album.getArtists().size() > 0);
        assertTrue(album.getSongs().size() > 0);
        assertEquals(provider, album.getMusicProvider());
    }
}
