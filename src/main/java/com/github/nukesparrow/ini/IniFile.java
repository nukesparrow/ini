/*
 * Copyright 2015 Nuke Sparrow <nukesparrow@bitmessage.ch>.
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
package com.github.nukesparrow.ini;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public final class IniFile extends Ini {

    private static final Logger LOG = Logger.getLogger(IniFile.class.getName());

    public static Ini initialize(String path) {
        try {
            return getIni(new File(path));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new Ini();
        }
    }

    private static final HashMap<File, IniFile> instances = new HashMap<File, IniFile>();

    public static synchronized IniFile getIni(String file) throws IOException {
        return getIni(new File(file));
    }

    public static synchronized IniFile getIni(File file) throws IOException {
        IniFile ini = instances.get(file);
        if (ini == null)
            instances.put(file, ini = new IniFile(file));
        return ini;
    }

    public final File file;

    private IniFile(File file) throws IOException {
        this.file = file;
        if (file.exists()) {
            Ini ini = IniStringUtil.readIniAndCloseThen(new BufferedReader(new FileReader(file)));

            copyFrom(ini, true);
            clearModified();
        }
    }

    private boolean autoSaveEnabled = false;

    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }
    
    public void enableAutoSave() {
        setAutoSaveEnabled(true);
    }

    public void disableAutoSave() {
        setAutoSaveEnabled(false);
    }

    public void setAutoSaveEnabled(boolean autoSaveEnabled) {
        this.autoSaveEnabled = autoSaveEnabled;
        if (autoSaveEnabled)
            try {
                saveIfModified();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
    }

    @Override
    protected synchronized void modified() {
        super.modified();
        if (autoSaveEnabled)
            try {
                save();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
    }
    
    public void saveIfModified() throws IOException {
        if (isModified())
            save();
    }

    public void save() throws IOException {
        File tmpFile = new File(file.getPath() + "~");
        BufferedWriter w = new BufferedWriter(new FileWriter(tmpFile));
        try {
            try {
                IniStringUtil.buildIni(w, this);
                clearModified();
            } finally {
                w.close();
            }
            if (!tmpFile.renameTo(file)) {
                file.delete();
                if (!tmpFile.renameTo(file)) {
                    throw new IOException("Unable to save config file (data: " + IniStringUtil.iniToString(this) + '"');
                }
            }
        } catch (IOException ex) {
            tmpFile.delete();
            throw new IOException("Unable to save config file (data: " + IniStringUtil.iniToString(this) + '"', ex);
        }
    }

}
