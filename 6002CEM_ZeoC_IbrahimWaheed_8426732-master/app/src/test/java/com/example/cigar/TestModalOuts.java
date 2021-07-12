package com.example.cigar;


import org.junit.Test;
import org.testng.annotations.AfterTest;

import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

public class TestModalOuts {

    @Test
    public void getCigarBox_StringWillReturn(){
        CigarModel model = new CigarModel();
        model.setCigarBox("String");
        assertEquals(model.getCigarBox(),"String");


    }
    @Test
    public  void getEmail_StringWillReturn(){
        DatabaseMapper model = new DatabaseMapper();
        model.setEmail("String");
        assertEquals(model.getEmail(),"String");
    }
    @Test
    public  void getUsername_StringWillReturn(){
        DatabaseMapper model = new DatabaseMapper();
        model.setUsername("String");
        assertEquals(model.getUsername(),"String");
    }




}


