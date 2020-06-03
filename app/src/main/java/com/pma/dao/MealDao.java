package com.pma.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.pma.model.DailySummary;
import com.pma.model.Meal;
import com.pma.model.MealPairRelation;

import java.util.Date;
import java.util.List;

@Dao
public interface MealDao {

    @Insert
    long insert(Meal meal);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);

    @Transaction
    @Query("SELECT * FROM meal WHERE meal.id = :id")
    MealPairRelation getMealWithPairs(int id);


    @Query("SELECT * FROM meal WHERE meal.id = :id")
    Meal getMeal(int id);

    @TypeConverters({DateStringConverter.class})
    @Query("SELECT strftime('%d-%m-%Y', dateAndTime/1000,'unixepoch') as day, SUM(totalKcal) as kcalIn FROM meal GROUP BY strftime('%d-%m-%Y', dateAndTime/1000,'unixepoch')")
    List<DailySummary> getDailySummariesKcalIn();

    @Query("SELECT * FROM meal WHERE strftime('%d-%m-%Y', dateAndTime/1000,'unixepoch') = :date")
    List<Meal> getMealsByDay(String date);
}