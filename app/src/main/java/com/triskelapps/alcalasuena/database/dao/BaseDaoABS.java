package com.triskelapps.alcalasuena.database.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class BaseDaoABS<T> {

    @Insert(onConflict = REPLACE)
    public abstract void insertAll(List<T> list);

    /**
     * If the item is replaced, a new _id will be assigned automatically. It is internally a delete/insert operation
     * @return the row id assigned
     */
    @Insert(onConflict = REPLACE)
    public abstract long insert(T item);

    /**
     * @return number of rows updated
     */
    @Update
    public abstract int update(T... item);

    /**
     * @return number of rows updated
     */
    @Update
    public abstract int updateAll(List<T> list);

    @Delete
    public abstract int delete(T... item);

    @Delete
    public abstract int deleteAll(List<T> list);
}
