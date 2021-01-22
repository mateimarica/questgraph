package com.questgraph.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDAO {
    @Query("SELECT * FROM Accounts")
    List<Account> getAccountList();

    @Insert
    void addAccount(Account ... account);

    @Query("SELECT COUNT(1) FROM Accounts WHERE number = :number")
    boolean accountExists(int number);

    @Update
    void updateAccount(Account account);

    @Delete
    void removeAccount(Account account);
}
