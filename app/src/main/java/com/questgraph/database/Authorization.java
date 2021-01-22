package com.questgraph.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Authorization {
    @PrimaryKey
    @NonNull
    String accessToken;
    //String tokenType;
    //int expiresIn;
    String refreshToken;
    String apiServer;

    public Authorization(String accessToken, String refreshToken, String apiServer) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.apiServer = apiServer;
    }
}
