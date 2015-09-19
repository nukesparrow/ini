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

import java.util.*;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class Ini implements IniProperties {
    
    private LinkedHashMap<String, Section> sections = new LinkedHashMap<String, Section>();

    /**
     * Modifications counter used to check if stored .ini update is necessary
     */
    private int version = 0;
    private int storedVersion = -1;

    protected void modified() {
        version++;
    }

    public void clearModified() {
        storedVersion = version;
    }

    public boolean isModified() {
        return version != storedVersion;
    }

    public int getVersion() {
        return version;
    }

    public Section section(String sectionName) {
        return section(sectionName, false, true);
    }

    public List<Section> sections() {
        return Collections.unmodifiableList(new ArrayList<Section>(sections.values()));
    }

    public Section section(String sectionName, boolean allocateIfNecessary) {
        return section(sectionName, allocateIfNecessary, false);
    }

    public Section section(String sectionName, boolean allocateIfNecessary, boolean lazy) {
        if ("".equals(sectionName) || "null".equals(sectionName))
            sectionName = null;

        Section s = sections.get(sectionName);
        if (s != null)
            return s;
        if (allocateIfNecessary) {
            s = new IniSection(sectionName);
            sections.put(sectionName, s);
            modified();
            return s;
        } else {
            return lazy ? new LazilyInitializedSection(sectionName) : null;
        }
    }

    public void removeSection(String s) {
        sections.remove(s);
        modified();
    }

    public void clear() {
        sections.clear();
        modified();
    }

    public String get(String key, String def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public byte get(String key, byte def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public short get(String key, short def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public int get(String key, int def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public long get(String key, long def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public boolean get(String key, boolean def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public float get(String key, float def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public double get(String key, double def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public Date get(String key, Date def) {
        Section s = section(null, false);
        return s == null ? def : s.get(key, def);
    }

    public boolean isSet(String key) {
        Section s = section(null, false);
        return s == null ? false : s.isSet(key);
    }

    public String get(String key) {
        Section s = section(null, false);
        return s == null ? null : s.get(key);
    }

    public void set(String key, Object value) {
        Section s = section(null, true);
        s.set(key, value);
    }

    public void set(String key, Object value, String comment) {
        Section s = section(null, true);
        s.set(key, value, comment);
    }

    public List<String> unnamedList() {
        Section s = section(null, false);
        return s == null ? Collections.EMPTY_LIST : s.unnamedList();
    }

    public interface Section extends IniProperties {
        
        public int getVersion();
        public void set(String key, Object value);
        public void set(String key, Object value, String comment);
        public String getName();
        public String getComment(String key);
        public void setComment(String key, String comment);
        public String getComment();
        public void setComment(String comment);
        public void remove(String key);
        public List<SectionEntry> properties();
        public SectionEntry getUnnamed(int index);
        public List<String> unnamedList();
        public void setUnnamedComment(String comment);
        public String getUnnamedComment();
        public void setUnnamed(String[] elems);

    }
    
    private abstract class AbstractSection implements Section {
        private List<String> unnamedList = null;

        public List<String> unnamedList() {

            return unnamedList != null ? unnamedList : (unnamedList = new AbstractList<String>() {

                @Override
                public boolean add(String e) {
                    String nc = Integer.toString(size() + 1);
                    AbstractSection.this.set("count", nc);
                    AbstractSection.this.set(nc, e);
                    return true;
                }

                @Override
                public String get(int index) {
                    return getUnnamed(index).getValue();
                }

                @Override
                public String set(int index, String element) {
                    String strIndex = Integer.toString(index + 1);
                    if (size() < (index - 1))
                        AbstractSection.this.set("count", strIndex);
                    String prev = AbstractSection.this.get(strIndex);
                    AbstractSection.this.set(strIndex, element);
                    return prev;
                }

                @Override
                public int size() {
                    return AbstractSection.this.get("count", 0);
                }

                @Override
                public void clear() {
                    int c = size();
                    for(int i = 0; i < c; i++) {
                        AbstractSection.this.remove(Integer.toString(i + 1));
                    }
                    AbstractSection.this.remove("count");
                }

            });

        }
    }

    public interface SectionEntry {
        public Section getSection();
        public String getName();
        public String getValue();
        public void setValue(String value);
        public String getComment();
        public void setComment(String comment);
        public boolean isUnnamed();
    }

    public class IniSection extends AbstractSection {

        private final String name;

        public IniSection(String name) {
            this.name = name;
        }

        private LinkedHashMap<String, IniSection.IniSectionEntry> entries = new LinkedHashMap<String, IniSectionEntry>();
        private String comment = null;

        private int version = 0;
        
        private void modified() {
            Ini.this.modified();
            version++;
        }
        
        public int getVersion() {
            return version;
        }

        public class IniSectionEntry implements SectionEntry {

            private final String name;
            private String value;
            private String comment;

            public IniSectionEntry(String name, String value) {
                if (value == null)
                    throw new NullPointerException();
                this.name = name;
                this.value = value;
                this.comment = null;
            }

            public IniSectionEntry(String name, String value, String comment) {
                if (value == null)
                    throw new NullPointerException();
                this.value = value;
                this.comment = comment;
                this.name = name;
            }

            public Section getSection() {
                return IniSection.this;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                if (value == null)
                    throw new NullPointerException();
                if (value.equals(this.value))
                    return;
                this.value = value;
                modified();
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                if (comment == null ? this.comment == null : comment.equals(this.comment))
                    return;
                this.comment = comment;
                modified();
            }

            public boolean isUnnamed() {
                if ("count".equals(name))
                    return true;
                try {
                    int i = Integer.parseInt(name);
                    return i >= 1 && i <= unnamedList().size();
                } catch (NumberFormatException ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return value;
            }

        }

        public String get(String key, String def) {
            IniSectionEntry e = entries.get(key);
            return e == null ? def : e.getValue();
        }

        public byte get(String key, byte def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Byte.valueOf(e.getValue());
        }

        public short get(String key, short def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Short.valueOf(e.getValue());
        }

        public int get(String key, int def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Integer.valueOf(e.getValue());
        }

        public long get(String key, long def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Long.valueOf(e.getValue());
        }

        public boolean get(String key, boolean def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Boolean.valueOf(e.getValue());
        }

        public float get(String key, float def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Float.valueOf(e.getValue());
        }

        public double get(String key, double def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : Double.valueOf(e.getValue());
        }

        public Date get(String key, Date def) {
            IniSectionEntry e = entries.get(key);
            return e == null || e.getValue().isEmpty() ? def : new Date(e.getValue());
        }

        public boolean isSet(String key) {
            return entries.containsKey(key);
        }

        public String get(String key) {
            IniSectionEntry e = entries.get(key);
            return e == null ? null : e.getValue();
        }

        public void set(String key, Object value) {
            IniSectionEntry e = entries.get(key);
            if (e == null) {
                entries.put(key, new IniSectionEntry(key, String.valueOf(value)));
                modified();
            } else
                e.setValue(String.valueOf(value));
        }

        public void set(String key, Object value, String comment) {
            IniSectionEntry e = entries.get(key);
            if (e == null) {
                entries.put(key, new IniSectionEntry(key, String.valueOf(value), comment));
                modified();
            } else {
                e.setValue(String.valueOf(value));
                e.setComment(comment);
            }
        }

        public String getName() {
            return name;
        }

        public String getComment(String key) {
            IniSectionEntry e = entries.get(key);
            return e == null ? null : e.getComment();
        }

        public void setComment(String key, String comment) {
            IniSectionEntry e = entries.get(key);
            if (e == null) {
                throw new IllegalArgumentException("Field " + key + " not found");
            } else
                e.setComment(comment);
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            if (this.comment == null ? comment == null : this.comment.equals(comment))
                return;
            this.comment = comment;
            modified();
        }

        public void remove(String key) {
            entries.remove(key);
            modified();
        }

        public Set<String> keys() {
            return entries.keySet();
        }

        public List<SectionEntry> properties() {
            return Collections.unmodifiableList(new ArrayList(entries.values()));
        }

        public SectionEntry getUnnamed(int index) {
            return entries.get(Integer.toString(index + 1));
        }

        public void setUnnamedComment(String comment) {
            if (!isSet("count"))
                set("count", "0", comment);
            else
                setComment("count", comment);
        }

        public String getUnnamedComment() {
            return getComment("count");
        }

        public void setUnnamed(String[] elems) {
            String savedComment = getUnnamedComment();
            unnamedList().clear();
            unnamedList().addAll(Arrays.asList(elems));
            setUnnamedComment(savedComment);
        }

        @Override
        public String toString() {
            return entries.toString();
        }

    }

    private class LazilyInitializedSection extends AbstractSection {
        
        private final String sectionName;
        private IniSection section = null;

        public LazilyInitializedSection(String sectionName) {
            this.sectionName = sectionName;
        }

        private IniSection getSection() {
            if (section != null)
                return section;
            section = (IniSection) Ini.this.section(sectionName, false, false);
            return section;
        }

        private IniSection section() {
            if (section != null)
                return section;
            section = (IniSection) Ini.this.section(sectionName, true, false);
            return section;
        }

        public int getVersion() {
            return getSection() == null ? 0 : section.getVersion();
        }

        public void set(String key, Object value) {
            section().set(key, value);
        }

        public void set(String key, Object value, String comment) {
            section().set(key, value, comment);
        }

        public String getName() {
            return sectionName;
        }

        public String getComment(String key) {
            return getSection() == null ? null : section.getComment(key);
        }

        public void setComment(String key, String comment) {
            section().setComment(key, comment);
        }

        public String getComment() {
            return getSection() == null ? null : section.getComment();
        }

        public void setComment(String comment) {
            section().setComment(comment);
        }

        public void remove(String key) {
            if (getSection() != null)
                section.remove(key);
        }

        public List<SectionEntry> properties() {
            return getSection() == null ? Collections.EMPTY_LIST : section.properties();
        }

        public SectionEntry getUnnamed(int index) {
            return getSection() == null ? null : section.getUnnamed(index);
        }

        public List<String> unnamedList() {
            return getSection() == null ? super.unnamedList() : section.unnamedList();
        }

        public void setUnnamedComment(String comment) {
            section().setUnnamedComment(comment);
        }

        public String getUnnamedComment() {
            return getSection() == null ? null : section.getUnnamedComment();
        }

        public void setUnnamed(String[] elems) {
            section().setUnnamed(elems);
        }

        public String get(String key, String def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public byte get(String key, byte def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public short get(String key, short def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public int get(String key, int def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public long get(String key, long def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public boolean get(String key, boolean def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public float get(String key, float def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public double get(String key, double def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public Date get(String key, Date def) {
            return getSection() == null ? def : section.get(key, def);
        }

        public boolean isSet(String key) {
            return getSection() == null ? false : section.isSet(key);
        }

        public String get(String key) {
            return getSection() == null ? null : section.get(key);
        }

        @Override
        public String toString() {
            return getSection() == null ? "#lazily initialized section\n["+ sectionName + "]\n" : section.toString();
        }

    }

    @Override
    public String toString() {
        return IniStringUtil.iniToString(this);
    }

    public void copyFrom(Ini source, boolean overwrite) {
        for (Section section : source.sections()) {
            Ini.Section destSection = section(section.getName());
            if (overwrite || destSection.getComment() == null)
                destSection.setComment(section.getComment());
            for (SectionEntry entry : section.properties()) {
                String name = entry.getName();
                if (overwrite || !destSection.isSet(name)) {
                    destSection.set(name, entry.getValue(), entry.getComment());
                }
            }
        }
    }

}
