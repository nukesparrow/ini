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

import com.github.nukesparrow.collections.IntegerArrayList;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String[] split(char splitter, String string) {
        return split(splitter, string, Integer.MAX_VALUE);
    }

    public static String[] split(char splitter, String string, int limit) {
        // TODO : Implement splitter for short strings
        return splitLong(splitter, string, limit);
    }

    public static String[] splitLong(char splitter, String string, int limit) {
        if (string.isEmpty())
            return EMPTY_STRING_ARRAY;
        if (limit == 1)
            return new String[] {string};

        IntegerArrayList s = new IntegerArrayList();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == splitter) {
                s.add(i);
                if (limit >= 0 && s.size() == (limit-1))
                    break;
            }
        }
        if (s.isEmpty())
            return new String[] { string };
        String[] r = new String[s.size() + 1];
        r[0] = string.substring(0, s.getInt(0));
        for(int i = 1; i < s.size(); i++)
            r[i] = string.substring(s.getInt(i - 1) + 1, s.getInt(i));
        r[s.size()] = string.substring(s.getInt(s.size() - 1) + 1);
        return r;
    }

    public static String[] splitLong(String splitter, String string, int limit) {
        if (string.isEmpty())
            return EMPTY_STRING_ARRAY;
        if (splitter.isEmpty())
            throw new UnsupportedOperationException();
        if (limit == 1)
            return new String[] {string};

        IntegerArrayList s = new IntegerArrayList();
        int sl = splitter.length();
        int i = 0;
        int e = string.length() - sl + 1;
        while (i < e) {
            if (string.substring(i, i + sl).equals(splitter)) {
                s.add(i);
                if (limit >= 0 && s.size() == (limit-1))
                    break;
                i+=sl;
            } else {
                i++;
            }
        }
        if (s.isEmpty())
            return new String[] { string };
        String[] r = new String[s.size() + 1];
        r[0] = string.substring(0, s.getInt(0));
        for(i = 1; i < s.size(); i++)
            r[i] = string.substring(s.getInt(i - 1) + sl, s.getInt(i));
        r[s.size()] = string.substring(s.getInt(s.size() - 1) + sl);
        return r;
    }

    public static String replaceAll(String string, String what, String with) {
        String[] s = splitLong(what, string, Integer.MAX_VALUE);
        if (s.length == 1)
            return string;
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (i > 0)
                r.append(with);
            r.append(s[i]);
        }
        return r.toString();
    }
    
    public static String join(String splitter, String... strings) {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < strings.length; i++) {
            if (i > 0)
                b.append(splitter);
            b.append(strings[i]);
        }
        return b.toString();
    }

    public static String join(char splitter, String... strings) {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < strings.length; i++) {
            if (i > 0)
                b.append(splitter);
            b.append(strings[i]);
        }
        return b.toString();
    }

    public static void main(String[] args) {
        System.out.println(replaceAll("1,2,3,4,5,6,7,8,9", ",2,", "!!!"));
//        for (Object s : split(',', "1,2,3,4,5", 6)) {
//            System.out.println(s);
//        }
    }

}
