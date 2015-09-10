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

import java.util.List;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IniBuilder {

    public final Ini ini;
    private Ini.Section section;

    public IniBuilder() {
        ini = new Ini();
    }

    public IniBuilder(Ini ini) {
        this.ini = ini;
    }
    
    private IniBuilder(Ini ini, Ini.Section section) {
        this.ini = ini;
        this.section = section;
    }
    
    public IniBuilder copy(Ini i) {
        ini.copyFrom(i, true);
        return this;
    }

    public IniBuilder section(String name, String comment) {
        Ini.Section s = ini.section(name, true);
        s.setComment(comment);
        return new IniBuilder(ini, s);
    }

    public IniBuilder section(String name) {
        Ini.Section s = ini.section(name);
        return new IniBuilder(ini, s);
    }

    public IniBuilder put(String name, String value, String comment) {
        if (section == null)
            section = ini.section(null);
        section.set(name, value, comment);
        return this;
    }

    public IniBuilder put(String name, String value) {
        if (section == null)
            section = ini.section(null);
        section.set(name, value);
        return this;
    }

    public IniBuilder put(List<String> values) {
        if (section == null)
            section = ini.section(null);
        section.unnamedList().addAll(values);
        return this;
    }

    public IniBuilder nothing() {
        return this;
    }

}
