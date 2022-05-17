package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.News;

import java.util.List;


@Dao
public abstract class BandDao implements BaseDao<Band> {


    @Query("SELECT * FROM band")
    public abstract List<Band> getAll();

    @Query("SELECT * FROM band WHERE id = :id")
    public abstract Band getBandById(int id);

    public Band getBandWithTagById(int id) {
        Band band = getBandById(id);
        band.setTag(App.getDB().tagDao().getTagById(band.getIdTag()));
        return band;
    }

    @Query("SELECT * FROM band WHERE name LIKE '%' ||  :text || '%' ")
    public abstract List<Band> filterByName(String text);

    @Query("DELETE FROM band")
    public abstract void deleteAll();

}
