package com.example.cigar;

import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Patterns;




import org.junit.Before;

import org.junit.runner.RunWith;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;

import org.powermock.core.classloader.annotations.PrepareForTest;

import org.powermock.modules.junit4.PowerMockRunner;


import static com.example.cigar.RegisterFragment.isValidEmail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import static org.junit.Assert.assertFalse;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)

@PrepareForTest({TextUtils.class, Patterns.class})
public class Vaildation {

    // validation for email


    @Before
    public void setup() {
        mockStatic(Patterns.class);
        mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(any(CharSequence.class)) &&  Patterns.EMAIL_ADDRESS.matcher(any(CharSequence.class)).matches()).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                CharSequence str = (CharSequence) invocation.getArguments()[0];
                if (str == null || str.length() == 0)
                    return true;
                else
                    return false;
            }
        });

    }


    public ArrayList<String> batchEmailPositive() {
        // call the regsiter fragement and test out isValidEmails


        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("D:/6002Android/IbrahimWaheed_8426732_ZeoC/app/src/test/java/com/example/cigar/emails.txt"
            ));
            ArrayList<String> Emails = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                Emails.add(line);

            }
           Emails.remove(null);
            return Emails;

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchEmailPositive();
    }




    @Test
    public void TestEmailsCases(){
        for (int i = 0; i < batchEmailPositive().size() ; i++) {
            System.out.println(batchEmailPositive().get(i));
            assertFalse(isValidEmail(batchEmailPositive().get(i)));
            }

        }

    }







