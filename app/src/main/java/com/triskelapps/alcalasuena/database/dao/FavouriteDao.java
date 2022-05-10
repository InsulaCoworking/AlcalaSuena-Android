package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Favourite;

import java.util.List;


@Dao
public interface FavouriteDao extends BaseDao<Favourite> {


    @Query("SELECT * FROM favourite")
    List<Favourite> getAll();

    @Query("DELETE FROM favourite")
    void deleteAll();

    @Query("SELECT * FROM favourite WHERE idEvent = :idEvent")
    Favourite getFavouriteEvent(int idEvent);

    @Query("UPDATE favourite SET starred = 1 WHERE idEvent IN (:idsFavsEvents)")
    void setEventsStarred(Integer[] idsFavsEvents);

    @Query("SELECT starred FROM favourite WHERE idEvent = :idEvent")
    boolean isStarred(int idEvent);
}
