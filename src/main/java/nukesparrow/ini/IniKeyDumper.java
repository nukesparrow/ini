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
package nukesparrow.ini;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IniKeyDumper extends IniWrapper {

    public final Set<Pair> pairs = Collections.synchronizedSet(new LinkedHashSet<Pair>());

    public IniKeyDumper(IniProperties ini) {
        super(ini);
    }

    public class Pair {
        public final Class type;
        public final String key;
        public final Object value;

        public Pair(Class type, String key, Object value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pair other = (Pair) obj;
            if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
                return false;
            }
            if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + (this.key != null ? this.key.hashCode() : 0);
            hash = 37 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "#" + (type == null ? "no type" : type.toString()) + "\n" +
                   (value == null ? "#" : "") + key + "=" + (value != null ? value.toString() : "") + "\n";
        }

    }

    private void newPair(Class type, String key, Object def) {
        pairs.add(new Pair(type, key, def));
    }

    @Override
    public String get(String key, String def) {
        newPair(String.class, key, def);
        return super.get(key, def);
    }

    @Override
    public byte get(String key, byte def) {
        newPair(Byte.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public short get(String key, short def) {
        newPair(Short.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public int get(String key, int def) {
        newPair(Integer.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public long get(String key, long def) {
        newPair(Long.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public boolean get(String key, boolean def) {
        newPair(Boolean.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public float get(String key, float def) {
        newPair(Float.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public double get(String key, double def) {
        newPair(Double.TYPE, key, def);
        return super.get(key, def);
    }

    @Override
    public Date get(String key, Date def) {
        newPair(Date.class, key, def);
        return super.get(key, def);
    }

    @Override
    public String get(String key) {
        newPair(String.class, key, null);
        return super.get(key);
    }

    @Override
    public boolean isSet(String key) {
        newPair(null, key, null);
        return super.isSet(key);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Pair pair : pairs) {
            s.append(pair);
        }
        return s.toString();
    }

}
