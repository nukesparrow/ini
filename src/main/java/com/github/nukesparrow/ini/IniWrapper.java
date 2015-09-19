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

import java.util.Date;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public abstract class IniWrapper implements IniProperties {

    protected final IniProperties ini;

    protected IniWrapper(IniProperties ini) {
        this.ini = ini;
    }

    public String get(String key, String def) {
        return ini.get(key, def);
    }

    public byte get(String key, byte def) {
        return ini.get(key, def);
    }

    public short get(String key, short def) {
        return ini.get(key, def);
    }

    public int get(String key, int def) {
        return ini.get(key, def);
    }

    public long get(String key, long def) {
        return ini.get(key, def);
    }

    public boolean get(String key, boolean def) {
        return ini.get(key, def);
    }

    public float get(String key, float def) {
        return ini.get(key, def);
    }

    public double get(String key, double def) {
        return ini.get(key, def);
    }

    public Date get(String key, Date def) {
        return ini.get(key, def);
    }

    public boolean isSet(String key) {
        return ini.isSet(key);
    }

    public String get(String key) {
        return ini.get(key);
    }
}
