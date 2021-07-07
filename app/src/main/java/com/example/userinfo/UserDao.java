package com.example.userinfo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void rgister(UserEntity userEntity);

    @Query("SELECT * FROM users where userName=(:userName) and pwd=(:password) ")
    UserEntity login(String userName, String password);
}
