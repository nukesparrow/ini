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
public interface IniProperties {
    public static final IniProperties EMPTYINIPROPERTIES = new IniProperties() {

        public String get(String key, String def) {
            return def;
        }

        public byte get(String key, byte def) {
            return def;
        }

        public short get(String key, short def) {
            return def;
        }

        public int get(String key, int def) {
            return def;
        }

        public long get(String key, long def) {
            return def;
        }

        public boolean get(String key, boolean def) {
            return def;
        }

        public float get(String key, float def) {
            return def;
        }

        public double get(String key, double def) {
            return def;
        }

        public Date get(String key, Date def) {
            return def;
        }

        public boolean isSet(String key) {
            return false;
        }

        public String get(String key) {
            return null;
        }
    };

    public String get(String key, String def);
    public byte get(String key, byte def);
    public short get(String key, short def);
    public int get(String key, int def);
    public long get(String key, long def);
    public boolean get(String key, boolean def);
    public float get(String key, float def);
    public double get(String key, double def);
    public Date get(String key, Date def);

    public boolean isSet(String key);
    public String get(String key);

}
