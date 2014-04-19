/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.epfl.data.bigdata.markov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author Max
 */
public class Tweet {
    public String text;
    public String lang;
    
    
    private final static ObjectMapper om = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    public static Tweet parse(String s) 
            throws IOException, JsonProcessingException {
        return om.readValue(s, Tweet.class);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
}
