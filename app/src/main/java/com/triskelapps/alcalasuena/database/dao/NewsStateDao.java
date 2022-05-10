package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.model.NewsState;

import java.util.List;


@Dao
public interface NewsStateDao extends BaseDao<NewsState> {


    @Query("SELECT * FROM newsstate")
    List<NewsState> getAll();

    @Query("DELETE FROM newsstate")
    void deleteAll();

}
