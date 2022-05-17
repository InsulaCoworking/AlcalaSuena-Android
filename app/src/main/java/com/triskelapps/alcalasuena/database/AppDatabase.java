package com.triskelapps.alcalasuena.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.work.impl.model.WorkTagDao;

import com.triskelapps.alcalasuena.database.dao.BandDao;
import com.triskelapps.alcalasuena.database.dao.EventDao;
import com.triskelapps.alcalasuena.database.dao.FavouriteDao;
import com.triskelapps.alcalasuena.database.dao.NewsDao;
import com.triskelapps.alcalasuena.database.dao.NewsStateDao;
import com.triskelapps.alcalasuena.database.dao.TagDao;
import com.triskelapps.alcalasuena.database.dao.TagStateDao;
import com.triskelapps.alcalasuena.database.dao.VenueDao;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Favourite;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.NewsState;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.model.TagState;
import com.triskelapps.alcalasuena.model.Venue;


@Database(version = 7, exportSchema = false, entities = {
        Band.class,
        Event.class,
        Favourite.class,
        News.class,
        NewsState.class,
        Tag.class,
        TagState.class,
        Venue.class,
})
public abstract class AppDatabase extends RoomDatabase {

    public abstract BandDao bandDao();

    public abstract EventDao eventDao();

    public abstract FavouriteDao favouriteDao();

    public abstract NewsDao newsDao();

    public abstract NewsStateDao newsStateDao();

    public abstract TagDao tagDao();

    public abstract TagStateDao tagStateDao();

    public abstract VenueDao venueDao();


}
