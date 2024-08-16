package com.example.appassign;

import android.view.MenuItem;

import androidx.annotation.NonNull;

public interface Navigation {
    boolean onOptionsItemSelected(MenuItem item);

    boolean onNavigationItemSelected(@NonNull MenuItem item);

    void onBackPressed();
}
