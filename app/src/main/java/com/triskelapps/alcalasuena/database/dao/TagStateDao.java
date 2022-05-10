package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.model.TagState;

import java.util.List;


@Dao
public interface TagStateDao extends BaseDao<TagState> {


    @Query("SELECT * FROM tagstate")
    List<TagState> getAll();

    @Query("SELECT * FROM tagstate WHERE idTag = :idTag")
    TagState getByIdTag(String idTag);

    @Query("UPDATE tagstate SET active = 1")
    void activeAll();

    @Query("UPDATE tagstate SET active = 0")
    void inactiveAll();

    @Query("UPDATE tagstate SET active = 1 WHERE idTag = :id")
    void setActive(String id);

    @Query("UPDATE tagstate SET active = NOT active WHERE idTag = :id")
    void toggleState(String id);

    @Query("SELECT * from tagstate WHERE active = 1")
    List<TagState> getActive();

    @Query("SELECT * from tagstate WHERE active = 0")
    List<TagState> getInactive();

    @Query("SELECT active from tagstate WHERE idTag = :id")
    boolean isTagActive(String id);

    @Query("DELETE FROM tagstate")
    void deleteAll();

}
