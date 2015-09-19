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
public class IniSubproperties implements IniProperties {

    public final IniProperties parent;
    public final String prefix;

    public IniSubproperties(IniProperties parent, String prefix) {
        this.parent = parent;
        this.prefix = prefix;
    }

    public String get(String key, String def) {
        return parent.get(prefix + key, def);
    }

    public byte get(String key, byte def) {
        return parent.get(prefix + key, def);
    }

    public short get(String key, short def) {
        return parent.get(prefix + key, def);
    }

    public int get(String key, int def) {
        return parent.get(prefix + key, def);
    }

    public long get(String key, long def) {
        return parent.get(prefix + key, def);
    }

    public boolean get(String key, boolean def) {
        return parent.get(prefix + key, def);
    }

    public float get(String key, float def) {
        return parent.get(prefix + key, def);
    }

    public double get(String key, double def) {
        return parent.get(prefix + key, def);
    }

    public Date get(String key, Date def) {
        return parent.get(prefix + key, def);
    }

    public boolean isSet(String key) {
        return parent.isSet(prefix + key);
    }

    public String get(String key) {
        return parent.get(prefix + key);
    }

}
