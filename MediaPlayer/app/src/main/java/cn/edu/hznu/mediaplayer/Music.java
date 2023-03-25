package cn.edu.hznu.mediaplayer;

public class Music {
    private int id;// 歌曲的id
    private String title;// 歌曲名
    private String artist;// 作者
    private int duration;// 时长
    private long size;// 大小
    private String album;// 专辑
    private String path;
    private long albumId;

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Music(int id, String title, String artist, int duration, long size, String album, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.path = path;
    }

    public Music(String title, String path, String album, String artist, long size, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.path = path;
    }

    public Music(int id, String title, String artist, int duration, long size, String album, String url, long albumId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.path = url;
        this.albumId=albumId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}


