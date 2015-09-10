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

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class StringIniParser {

    public final Ini ini;

    public StringIniParser(Ini ini) {
        if (ini == null)
            throw new NullPointerException();
        this.ini = ini;
    }

    private Ini.Section section = null;
    
    private Ini.Section section(String sn) {
        return section = ini.section(sn);
    }

    private Ini.Section section() {
        if (section == null)
            section = ini.section(null);
        return section;
    }

    public Ini.Section currentSection() {
        return section;
    }

    private StringBuilder comment = null;

    public void parseLine(String line) {
        if (line == null)
            throw new NullPointerException();
        line = line.trim();
        
        if (line.isEmpty()) {
            if (comment != null && section == null) {
                comment.trimToSize();
                section(null).setComment(comment.toString());
                comment = null;
            } else if (comment != null) {
                
            }
            return;
        }
        
        if (line.startsWith("#")) {
            if (comment == null)
                comment = new StringBuilder(line.substring(1));
            else
                comment.append('\n').append(line.substring(1));
            return;
        }
        
        if (line.startsWith("[")) {
            int i = line.indexOf(']', 1);
            if (i == -1)
                line = line.substring(1);
            else
                line = line.substring(1, i);
            line = line.trim();
            section(IniStringUtil.unescape(line));
            if (comment != null) {
                comment.trimToSize();
                section.setComment(comment.toString());
                comment = null;
            }
            return;
        }
        
        int i = line.indexOf('=');
        if (i == -1) {
            String index = Integer.toString(section().get("count", 0) + 1);
            if (comment != null) {
                comment.trimToSize();
                section.set(index, IniStringUtil.unescape(line), comment.toString());
                comment = null;
            } else {
                section.set(index, IniStringUtil.unescape(line));
            }
            section.set("count", index);
        } else {
            String key = IniStringUtil.unescape(line.substring(0, i).trim());
            String value = IniStringUtil.unescape(line.substring(i + 1).trim());
            if (comment != null) {
                comment.trimToSize();
                section().set(key, value, comment.toString());
                comment = null;
            } else {
                section().set(key, value);
            }
        }
    }

    public void parseAndCloseThen(BufferedReader r) throws IOException {
        try {
            parse(r);
        } finally {
            r.close();
        }
    }

    public void parse(BufferedReader r) throws IOException {
        String line;
        while ((line = r.readLine()) != null)
            parseLine(line);
    }

}
