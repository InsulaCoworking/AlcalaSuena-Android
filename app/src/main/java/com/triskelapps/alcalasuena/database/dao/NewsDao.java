package com.triskelapps.alcalasuena.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.model.News;

import java.util.List;


@Dao
public interface NewsDao extends BaseDao<News> {


    @Query("SELECT * FROM news")
    List<News> getAll();

    @Query("SELECT * FROM news ORDER BY start_date DESC")
    List<News> getAllSorted();

    @Query("SELECT * FROM news WHERE id = :id")
    News getNewsById(int id);


    @Query("DELETE FROM news")
    void deleteAll();

}
