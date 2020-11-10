package com.questgraph;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Accounts")
public class Account {
    final String type; //TFSA, RRSP, etc

    @PrimaryKey
    final int number;

    final String status; //Active, Suspended (Closed), Suspended (View Only), Liquidate Only, Closed

    final boolean isPrimary; //is this the primary account for the user

    final boolean isBilling; //Whether this account gets billed for various expenses such as fees, market data, etc.

    final String clientAccountType; //Individual, Joint, Family, etc

    public Account(String type, int number, String status, boolean isPrimary, boolean isBilling, String clientAccountType) {
        this.type = type;
        this.number = number;
        this.status = status;
        this.isPrimary = isPrimary;
        this.isBilling = isBilling;
        this.clientAccountType = clientAccountType;
    }

    @Ignore
    public Account(String type, String number, String status, String isPrimary, String isBilling, String clientAccountType) {
        this.type = type;
        this.number = Integer.parseInt(number);
        this.status = status;
        this.isPrimary = Boolean.parseBoolean(isPrimary);
        this.isBilling = Boolean.parseBoolean(isBilling);
        this.clientAccountType = clientAccountType;
    }
}
