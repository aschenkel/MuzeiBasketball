package com.schenkel.axel.muzeiplantae.DataAccess.Interfaces;

/**
 * Created by axel on 20/05/17.
 */

public interface LocalDBService {
    String GetNextID();

    int RestartID();

    int ReadSharedPrefID();
}
