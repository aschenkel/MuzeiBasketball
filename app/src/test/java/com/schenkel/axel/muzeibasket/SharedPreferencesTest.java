package com.schenkel.axel.muzeibasket;

import android.content.Context;
import android.content.SharedPreferences;

import com.schenkel.axel.muzeibasket.DataAccess.Implementations.SharedPreferencesService;

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

public class SharedPreferencesTest {

    private static final int START_ID_VALUE = 0;
    private static final String CORRECT_RETURN_VALUE = "1";
    private static final String ID_TAG = "ID";
    @Mock SharedPreferences sharedPrefs;
    @Mock Context context;
    SharedPreferencesService sharedPreferencesService;
    @Mock SharedPreferences.Editor mEditor;

    @Before
    public void before() throws Exception {

        InitMocks();

        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        when(sharedPrefs.edit()).thenReturn(mEditor);

        sharedPreferencesService = new SharedPreferencesService(this.context);
    }

    private void InitMocks() {
        this.mEditor = Mockito.mock(SharedPreferences.Editor.class);
        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
        this.context = Mockito.mock(Context.class);
    }

    @Test
    public void UpdateCurrentIDTest() throws Exception {
        when(mEditor.putString(ID_TAG, "")).thenReturn(mEditor);
        when(sharedPreferencesService.ReadSharedPrefID()).thenReturn(START_ID_VALUE);
        assertEquals(CORRECT_RETURN_VALUE, sharedPreferencesService.GetNextID());
    }

    @Test
    public void ReestartIDTest() throws Exception {
        when(mEditor.putInt(ID_TAG, START_ID_VALUE)).thenReturn(mEditor);
        assertEquals(START_ID_VALUE, sharedPreferencesService.RestartID());
        verify(mEditor.putInt(ID_TAG, START_ID_VALUE));
    }

    @Test
    public void ReadSharedPrefIDTest() throws Exception {
        assertEquals(START_ID_VALUE, sharedPreferencesService.ReadSharedPrefID());
    }


}
