package com.example.axel.nbamuzei;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.axel.nbamuzei.DataAccess.SharedPreferencesService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by axel on 13/05/17.
 */

public class SharePreferencesTest {

    private static final int START_ID_VALUE = 0;
    private static final String CORRECT_RETURN_VALUE = "1";
    private static final String ID_TAG = "ID";
    @Mock SharedPreferences sharedPrefs;
    @Mock Context context;
    @Mock SharedPreferencesService sharedPreferencesService;
    @Mock SharedPreferences.Editor mEditor;

    @Before
    public void before() throws Exception {
        this.mEditor = Mockito.mock(SharedPreferences.Editor.class);
        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
        this.context = Mockito.mock(Context.class);

        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        when(sharedPrefs.edit()).thenReturn(mEditor);
        sharedPreferencesService = new SharedPreferencesService(this.context);
    }

    @Test
    public void UpdateCurrentID() throws Exception {
        when(mEditor.putString(ID_TAG, "")).thenReturn(mEditor);
        when(sharedPreferencesService.ReadSharedPrefID()).thenReturn(START_ID_VALUE);
        assertEquals(CORRECT_RETURN_VALUE, sharedPreferencesService.UpdateCurrentID());
    }

    @Test
    public void ReestartID() throws Exception {
        when(mEditor.putInt(ID_TAG, START_ID_VALUE)).thenReturn(mEditor);
        assertEquals(START_ID_VALUE, sharedPreferencesService.RestartID());
        verify(mEditor.putInt(ID_TAG, START_ID_VALUE));
    }

    @Test
    public void ReadSharedPrefID() throws Exception {
        when(sharedPrefs.getInt(ID_TAG, START_ID_VALUE)).thenReturn(START_ID_VALUE);
        assertEquals(START_ID_VALUE, sharedPreferencesService.ReadSharedPrefID());
    }


}
