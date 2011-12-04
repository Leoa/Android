/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itnoles.shared.util;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.itnoles.shared.util.base.SharedPreferenceSaver;

public class FroyoSharedPreferenceSaver extends SharedPreferenceSaver {
    protected BackupManager mBackupManager;

    public FroyoSharedPreferenceSaver(Context context) {
        super(context);
        mBackupManager = new BackupManager(context);
    }

    @Override
    public void savePreferences(Editor editor, boolean backup) {
        editor.commit();
        mBackupManager.dataChanged();
    }
}