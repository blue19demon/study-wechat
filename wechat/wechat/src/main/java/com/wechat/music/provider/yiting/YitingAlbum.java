package com.wechat.music.provider.yiting;

import com.alibaba.fastjson.annotation.JSONField;
import com.wechat.music.api.MusicProvider;
import com.wechat.music.model.Album;
import com.wechat.music.model.Artist;
import com.wechat.music.model.BaseBean;
import com.wechat.music.model.Song;
import com.wechat.music.util.SongUtils;

import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
class YitingAlbum extends BaseBean implements Album {

    public String name;

    public String id;

    @JSONField(name = "artists")
    public List<YitingArtist> artists;

    public List<YitingSong> songs;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAlbumId() {
        return id;
    }

    @Override
    public List<? extends Song> getSongs() {
        return songs;
    }

    public void setSongs(List<YitingSong> songs) {
        this.songs = songs;
    }

    @Override
    public List<? extends Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<YitingArtist> artists) {
        this.artists = artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(getArtists());
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Yiting;
    }
}
