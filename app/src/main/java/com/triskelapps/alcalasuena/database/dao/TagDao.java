package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Tag;

import java.util.List;


@Dao
public abstract class TagDao implements BaseDao<Tag> {


    @Query("SELECT * FROM tag")
    protected abstract List<Tag> getAllRaw();

    public List<Tag> getAll() {
        List<Tag> tags = getAllRaw();
        tags.stream().forEach(tag -> tag.setActive(App.getDB().tagStateDao().isTagActive(tag.getId())));
        return tags;
    }


    @Query("DELETE FROM tag")
    public abstract void deleteAll();

}
