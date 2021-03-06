/*
 * Copyright (C) 2011 Jonathan Steele
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.itnoles.shared.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.itnoles.shared.R;
import com.itnoles.shared.provider.ScheduleProvider;

public class ScheduleFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SCHEDULE_LOADER = 0x1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] projection = {ScheduleProvider.DATE, ScheduleProvider.TIME, ScheduleProvider.SCHOOL, ScheduleProvider.LOCATION};
        setListAdapter(new SimpleCursorAdapter(getActivity(), R.layout.schedule_item, null, projection,
            new int[] {R.id.date, R.id.time, R.id.school, R.id.location}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));

        getLoaderManager().initLoader(SCHEDULE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] projection = {"_id", ScheduleProvider.DATE, ScheduleProvider.TIME, ScheduleProvider.SCHOOL, ScheduleProvider.LOCATION};
        return new CursorLoader(getActivity(), ScheduleProvider.SCHEDULE_CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }
}