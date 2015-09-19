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
package com.github.nukesparrow.util;

import com.github.nukesparrow.ini.IniProperties;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class MapCaster implements IniProperties {

    static public String mapGet(Map map, Object key, String def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res.toString();
    }

    static public byte mapGet(Map map, Object key, byte def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Number ? ((Number)res).byteValue() : Byte.valueOf(res.toString());
    }

    static public short mapGet(Map map, Object key, short def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Number ? ((Number)res).shortValue() : Short.valueOf(res.toString());
    }

    static public int mapGet(Map map, Object key, int def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Number ? ((Number)res).intValue() : Integer.valueOf(res.toString());
    }

    static public long mapGet(Map map, Object key, long def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Number ? ((Number)res).longValue() : Long.valueOf(res.toString());
    }

    static public char mapGet(Map map, Object key, char def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Character ? ((Character)res).charValue() : res.toString().charAt(0);
    }

    static public boolean mapGet(Map map, Object key, boolean def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Boolean ? ((Boolean)res).booleanValue() : Boolean.valueOf(res.toString());
    }

    static public float mapGet(Map map, Object key, float def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Number ? ((Number)res).floatValue() : Float.valueOf(res.toString());
    }

    static public double mapGet(Map map, Object key, double def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res instanceof Number ? ((Number)res).doubleValue() : Double.valueOf(res.toString());
    }

    static public Object mapGet(Map map, Object key, Object def) {
        if (map == null)
            return def;
        Object res = map.get(key);
        if (res == null)
            return def;
        else
            return res;
    }

    static public Object[] mapGetArray(Map map, Object key, Object[] def) {
        if (map == null)
            return def;
        Object[] res = (Object [])map.get(key);
        if (res == null)
            return def;
        else
            return res;
    }

    static public <X extends Object> X[] mapGetArrayCast(Map map, Object key, X[] def) {
        if (map == null)
            return def;
        X[] res = (X [])map.get(key);
        if (res == null)
            return def;
        else
            return res;
    }

    private Map map;

    public MapCaster(Map map) {
        super();
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public String get(Object key, String def) {
        return mapGet(map, key, def);
    }

    public byte get(Object key, byte def) {
        return mapGet(map, key, def);
    }

    public short get(Object key, short def) {
        return mapGet(map, key, def);
    }

    public int get(Object key, int def) {
        return mapGet(map, key, def);
    }

    public long get(Object key, long def) {
        return mapGet(map, key, def);
    }

    public boolean get(Object key, boolean def) {
        return mapGet(map, key, def);
    }

    public float get(Object key, float def) {
        return mapGet(map, key, def);
    }

    public double get(Object key, double def) {
        return mapGet(map, key, def);
    }

    public Object get(Object key, Object def) {
        return mapGet(map, key, def);
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Object[] getArray(Object key, Object[] def) {
        return mapGetArray(map, key, def);
    }

    public <X extends Object> X[] getArrayCast(Object key, X[] def) {
        return mapGetArrayCast(map, key, def);
    }

    public String get(String key, String def) {
        return mapGet(map, key, def);
    }

    public byte get(String key, byte def) {
        return mapGet(map, key, def);
    }

    public short get(String key, short def) {
        return mapGet(map, key, def);
    }

    public int get(String key, int def) {
        return mapGet(map, key, def);
    }

    public long get(String key, long def) {
        return mapGet(map, key, def);
    }

    public boolean get(String key, boolean def) {
        return mapGet(map, key, def);
    }

    public float get(String key, float def) {
        return mapGet(map, key, def);
    }

    public double get(String key, double def) {
        return mapGet(map, key, def);
    }

    private DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

    public Date get(String key, Date def) {
        Object v = map.get(key);
        if (v instanceof Date) {
            return (Date)v;
        } else if (v instanceof String) {
            try {
                return dateFmt.parse((String) v);
            } catch (ParseException ex) {
                Logger.getLogger(MapCaster.class.getName()).log(Level.SEVERE, null, ex);
                return def;
            }
        } else if (v != null) {
            try {
                return dateFmt.parse(v.toString());
            } catch (ParseException ex) {
                Logger.getLogger(MapCaster.class.getName()).log(Level.SEVERE, null, ex);
                return def;
            }
        } else {
            return def;
        }
    }

    public boolean isSet(String key) {
        return map.containsKey(key);
    }

    public String get(String key) {
        Object r = map.get(key);
        if (r instanceof String)
            return (String)r;
        else if (r != null)
            return r.toString();
        else
            return null;
    }

    @Override
    public String toString() {
        return "MapCaster["+String.valueOf(map) +"]";
    }
    
}
