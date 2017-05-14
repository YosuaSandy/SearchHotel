package com.example.sandy.searchhotel;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by Sandy on 4/4/2017.
 */
public class PlaceDetail implements Serializable {
    @Key
    public String status;

    @Key
    public Place result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}
