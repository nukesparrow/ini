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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IniStringUtil {

    //<editor-fold defaultstate="collapsed" desc="Escaping routines">
    private static final char[] HEXCHARS = "0123456789ABCDEF".toCharArray();
    
    private static void escapeSection(Appendable a, String str) throws IOException {
        escape(a, str, true, false);
    }
    
    private static void escapeKey(Appendable a, String str) throws IOException {
        escape(a, str, false, true);
    }
    
    private static void escapeValue(Appendable a, String str) throws IOException {
        escape(a, str, false, false);
    }
    
    private static void escape(Appendable a, char c) throws IOException {
        a.append('%');
        a.append(HEXCHARS[(c & 0xF0) >>> 4]);
        a.append(HEXCHARS[c & 0xF]);
    }
    
    private static void escape(Appendable a, String str, boolean section, boolean key) throws IOException {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '%':
                case '\t':
                case '\r':
                case '\n':
                case '+':
                    escape(a, c);
                    break;
                case ']':
                    if (section)
                        escape(a, c);
                    else
                        a.append(c);
                    break;
                case '=':
                    if (key)
                        escape(a, c);
                    else
                        a.append(c);
                    break;
                case ' ':
                    if (i == 0 || i == (str.length()-1))
                        a.append('+');
                    else
                        a.append(' ');
                    break;
                default:
                    a.append(c);
            }
        }
    }
    
    /**
     * Escapes string, that can be used in .ini file
     * @param str unescaped string
     * @return Escaped value
     */
    public static String escape(String str) {
        StringBuilder b = new StringBuilder(str.length());
        try {
            escape(b, str, true, true);
        } catch (IOException shouldNeverHappen) {
        }
        return b.toString();
    }

    /**
     * Unescapes string, that can be used in .ini file
     * @param str escaped string
     * @return Unescaped value
     */
    public static String unescape(String str) {
        StringBuilder b = new StringBuilder(str.length());
        int i = 0;
        int l = str.length();
        while (l > 0) {
            char c = str.charAt(i);
            if (c == '+') {
                b.append(' ');
                i++;
                l--;
            } else {
                if (l >= 3 && c == '%') {
                    try {
                        b.append((char)Short.parseShort(str.substring(i + 1, i + 3), 16));
                        i+=3;
                        l-=3;
                    } catch (NumberFormatException ex) {
                        b.append(c);
                        i++;
                        l--;
                    }
                } else {
                    b.append(c);
                    i++;
                    l--;
                }
            }
        }
        b.trimToSize();
        return b.toString();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Text .ini file building routines">
    private static void buildComment(Appendable a, String comment, boolean newLine) throws IOException {
        if (newLine)
            a.append('\n');
        if (comment == null || comment.isEmpty())
            return;
        a.append("#").append(comment.trim().replaceAll("\\r?\\n", "\n#")).append("\n");
    }
    
    private static void buildEntry(Appendable a, String name, Ini.SectionEntry entry) throws IOException {
        buildComment(a, entry.getComment(), false);
        if (name == null) {
            escapeKey(a, entry.getValue());
        } else {
            escapeKey(a, name);
            a.append('=');
            escapeValue(a, entry.getValue());
        }
        a.append('\n');
    }
    
    private static void buildSection(Appendable a, String name, Ini.Section section) throws IOException {
        buildComment(a, section.getComment(), name != null);
        if (name == null) {
            a.append('\n');
        } else {
            a.append('[');
            escapeSection(a, name);
            a.append("]\n\n");
        }
        HashSet<String> skip = null;
        if (section.isSet("count")) {
            skip = new HashSet<String>();
            skip.add("count");
            int cnt = section.get("count", 0);
            for (int i = 1; i <= cnt; i++) {
                String k = Integer.toString(i);
                if (!section.isSet(k)) {
                    skip = null;
                    break;
                }
                skip.add(k);
            }
            if (skip != null) {
                for (int i = 0; i < cnt; i++) {
                    buildEntry(a, null, section.getUnnamed(i));
                }
            }
        }

        for (Ini.SectionEntry entry : section.properties()) {
            if (skip==null||!skip.contains(entry.getName()))
                buildEntry(a, entry.getName(), entry);
        }

    }
    
    public static String iniToString(Ini ini) {
        StringBuilder b = new StringBuilder();
        try {
            buildIni(b, ini);
        } catch (IOException shouldNeverHappen) {
        }
        return b.toString();
    }

    public static void buildIni(Appendable a, Ini ini) throws IOException {
        if (ini == null)
            return;
        Ini.Section s = ini.section(null);
        if (s != null)
            buildSection(a, null, s);
        for (Ini.Section section : ini.sections())
            if (section.getName() != null)
                buildSection(a, section.getName(), section);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=".ini parsing routines">
    public static Ini parseIni(String s) {
        try {
            return readIniAndCloseThen(new BufferedReader(new StringReader(s)));
        } catch (IOException ex) {
            throw new RuntimeException(ex); //should never happen
        }
    }
    
    public static void readIniAndCloseThen(BufferedReader r, Ini ini) throws IOException {
        StringIniParser parser = new StringIniParser(ini);
        parser.parseAndCloseThen(r);
    }
    
    public static Ini readIniAndCloseThen(BufferedReader r) throws IOException {
        Ini ini = new Ini();
        readIniAndCloseThen(r, ini);
        return ini;
    }
    //</editor-fold>

    public static void main(String[] args) throws Exception {
        Ini i = new Ini();
        i.section(null).setComment("Comment :)");
        i.section(null).set("test", "test value+", "Multiline comment :)\n;)");
        i.section("S1").setComment("Section comment");
        i.section("S1").set("te\rst", "  test\nvalue  ", "Multiline\tcomment :)\n;)");
        i.section("lst").set("count", 2);
        i.section("lst").set("1", "line1");
        i.section("lst").set("2", "line2");
        i.section("lst").unnamedList().add("line3");
        i.section("lst").unnamedList().add("line4");
        i.section("lst").unnamedList().add("line3");
        System.out.println(i.toString() + "\n---");

        System.out.println((parseIni(i.toString())).toString() + "\n---");
        
        StringBuilder t = new StringBuilder();
        for(char c = 0x20; c <= 0x7f; c++) {
            t.append(c);
        }
        if (!unescape(escape(t.toString())).equals(t.toString())) {
            System.err.println(t.toString());
            System.err.println(unescape(escape(t.toString())));
            System.err.println(escape(t.toString()));
        }
    }

}
