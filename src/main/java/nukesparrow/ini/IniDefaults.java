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

import nukesparrow.util.MapCaster;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IniDefaults implements IniProperties {

    protected final IniProperties wrapped;
    protected final IniProperties defaults;

    public IniDefaults(IniProperties wrapped, IniProperties defaults) {
        this.wrapped = wrapped == null ? EMPTYINIPROPERTIES : wrapped;
        this.defaults = defaults == null ? EMPTYINIPROPERTIES : defaults;
    }

    public IniDefaults(IniProperties wrapped, Map defaults) {
        this.wrapped = wrapped == null ? EMPTYINIPROPERTIES : wrapped;
        this.defaults = defaults == null ? EMPTYINIPROPERTIES : new MapCaster(defaults);
    }

    public String get(String key, String def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public byte get(String key, byte def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public short get(String key, short def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public int get(String key, int def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public long get(String key, long def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public boolean get(String key, boolean def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public float get(String key, float def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public double get(String key, double def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public Date get(String key, Date def) {
        return wrapped.get(key, defaults.get(key, def));
    }

    public boolean isSet(String key) {
        return wrapped.isSet(key) || defaults.isSet(key);
    }

    public String get(String key) {
        return wrapped.get(key, defaults.get(key));
    }

    @Override
    public String toString() {
        return "IniDefaults[Wrapped: ["+wrapped+"], Defaults: ["+defaults+"]]";
    }

}
