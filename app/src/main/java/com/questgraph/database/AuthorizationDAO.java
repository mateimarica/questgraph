package com.questgraph.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AuthorizationDAO {
    @Insert
    void insertAuthorization(Authorization authorization);

    @Query("DELETE FROM Authorization")
    void resetAuthorization();

    @Query("SELECT COUNT(*) FROM Authorization")
    int getNumberOfRows();

    @Query("SELECT accessToken FROM Authorization")
    String getAccessToken();

    @Query("SELECT refreshToken FROM Authorization")
    String getRefreshToken();

    @Query("SELECT apiServer From Authorization")
    String getApiServer();

    @Query("SELECT * From Authorization")
    Authorization getAuthorization();
}
