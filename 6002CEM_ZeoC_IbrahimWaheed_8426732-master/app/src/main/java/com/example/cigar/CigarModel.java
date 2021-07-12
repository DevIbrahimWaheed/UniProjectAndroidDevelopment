package com.example.cigar;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@IgnoreExtraProperties
public class CigarModel {
    String CigarBoxName;

    public CigarModel() {

    }
    public CigarModel(String CigarBoxName  ) {
        this.CigarBoxName = CigarBoxName;

    }


    public String getCigarBox(){
        return CigarBoxName;
    }


    public void setCigarBox(String cigarBox){
        this.CigarBoxName = cigarBox;
    }


    //Creates Dict/JSON object
    @Exclude
    public Map<String,Object> toJSON(){
        HashMap<String,Object> json  = new HashMap<>();
        return  json;


    }
}
