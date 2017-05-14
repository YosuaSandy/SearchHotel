package com.example.sandy.searchhotel;

import java.io.Serializable;

import java.util.List;

import com.google.api.client.util.Key;
/**
 * Created by Sandy on 3/30/2017.
 */
public class PlaceList implements Serializable{

    @Key
    public String status;

    @Key
    public List<Place> results;
}
