package com.triskelapps.alcalasuena.database.dao;

import static androidx.room.OnConflictStrategy.ABORT;
import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<T> {

    /**
     * If the item is replaced, a new _id will be assigned automatically. It is internally a delete/insert operation
     * @return the row id assigned
     */
    @Insert(onConflict = REPLACE)
    long insert(T item);

    @Insert(onConflict = REPLACE)
    void insertAll(List<T> list);


    /**
     * If the item exists, the operation is aborted
     * @return the row id assigned
     */
    @Insert(onConflict = IGNORE)
    long insertIfNotExists(T item);

    @Insert(onConflict = IGNORE)
    void insertAllIfNotExists(List<T> list);


    /**
     * @return number of rows updated
     */
    @Update
    int update(T... item);


    /**
     * @return number of rows updated
     */
    @Update
    int updateAll(List<T> list);

    @Delete
    int delete(T... item);

    @Delete
    int deleteAll(List<T> list);
}
