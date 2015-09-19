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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public abstract class AbstractIniProperties implements IniProperties {

    public String get(String key, String def) {
        String r = get(key);
        return r == null ? def : r;
    }

    public byte get(String key, byte def) {
        String r = get(key);
        return r == null ? def : Byte.parseByte(r);
    }

    public short get(String key, short def) {
        String r = get(key);
        return r == null ? def : Short.parseShort(r);
    }

    public int get(String key, int def) {
        String r = get(key);
        return r == null ? def : Integer.parseInt(r);
    }

    public long get(String key, long def) {
        String r = get(key);
        return r == null ? def : Long.parseLong(r);
    }

    public boolean get(String key, boolean def) {
        String r = get(key);
        return r == null ? def : Boolean.parseBoolean(r);
    }

    public float get(String key, float def) {
        String r = get(key);
        return r == null ? def : Float.parseFloat(r);
    }

    public double get(String key, double def) {
        String r = get(key);
        return r == null ? def : Double.parseDouble(r);
    }

    public Date get(String key, Date def) {
        String r = get(key);
        try {
            return r == null ? def : new SimpleDateFormat().parse(r);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
