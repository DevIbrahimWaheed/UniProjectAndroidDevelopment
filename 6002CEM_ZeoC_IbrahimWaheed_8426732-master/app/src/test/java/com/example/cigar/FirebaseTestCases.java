package com.example.cigar;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import org.apache.tools.ant.types.Resource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;


public class FirebaseTestCases {
    File JSON = new File("D:/6002Android/IbrahimWaheed_8426732_ZeoC/app/zeocigar-default-rtdb-export.json");

    public String ReadFile() {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(JSON));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            br.close();
            return builder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void databaseWillReturnUsers() throws JSONException {
        String Data = ReadFile();
        JSONObject reader = new JSONObject(Data);
        Iterator<String> keys = reader.keys();
        while (keys.hasNext()){
            String key = keys.next();
            if (reader.get(key) instanceof JSONObject) {
                String testuserData = "{\"boxes\":[null,\"test\"]}";
               assertEquals((((JSONObject) reader.get(key)).get("test")).toString(),testuserData);
            }
        }



    }
}
