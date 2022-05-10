package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;


@Dao
public interface VenueDao extends BaseDao<Venue> {


    @Query("SELECT * FROM venue")
    List<Venue> getAll();

    @Query("SELECT * FROM venue WHERE id = :id")
    Venue getVenueById(int id);

    @Query("DELETE FROM venue")
    void deleteAll();

}
