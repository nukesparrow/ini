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
package com.github.nukesparrow.virtfs;

import com.github.nukesparrow.ini.Ini;
import com.github.nukesparrow.ini.IniStringUtil;
import com.github.nukesparrow.util.StreamUtils;
import com.github.nukesparrow.util.StringUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public abstract class VFilesystem {
    
    public abstract InputStream openRawRead(String path) throws IOException;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public InputStream openRead(String path) throws IOException {
        InputStream in;
        try {
            in = new BufferedInputStream(openRawRead(path));
            if (path.endsWith(".gz"))
                return new GZIPInputStream(in, 1024*1024);
        } catch (IOException ex) {
            if (getAlternativeOutput() == null)
                throw ex;
            try {
                in = getAlternativeOutput().openRead(path);
            } catch (IOException ex1) {
                throw ex;
            }
        }
        return in;
    }

    public String getString(String path) throws IOException {
        InputStream in = openRead(path);
        return in == null ? null : StreamUtils.streamToString(in, true, UTF8);
    }

    public byte[] getBytes(String path) throws IOException {
        InputStream in = openRead(path);
        return in == null ? null : StreamUtils.streamToByteArray(in, true);
    }

    public Ini getIni(String path) throws IOException {
        InputStream in = openRead(path);
        
        return in == null ? null : IniStringUtil.parseIni(StreamUtils.streamToString(in, true, UTF8));
    }

    public File toFile(String path) {
        if (alternativeOutput != null)
            return alternativeOutput.toFile(path);
        return null;
    }

    private VFilesystem alternativeOutput = null;

    public VFilesystem getAlternativeOutput() {
        return alternativeOutput;
    }

    public void setAlternativeOutput(VFilesystem alternativeOutput) {
        this.alternativeOutput = alternativeOutput;
    }

    public OutputStream openRawWrite(String path, boolean append) throws IOException {
        throw new IOException("Attempt to write read only filesystem");
    }

    public OutputStream openWrite(String path, boolean append) throws IOException {
        if (alternativeOutput != null)
            return alternativeOutput.openWrite(path, append);
        return openRawWrite(path, append);
    }

    public VFile file(String path) {
        return new VFile(this, path);
    }

    public static String toRelativePath(String path) {
        if (path == null || path.isEmpty())
            return path;

        LinkedList<String> elements = new LinkedList<String>(Arrays.asList(StringUtils.split('/', path)));

        for (Iterator<String> it = elements.iterator(); it.hasNext();) {
            String element = it.next();
            if (".".equals(element) || element.isEmpty())
                it.remove();
        }

        for (Iterator<String> it = elements.descendingIterator(); it.hasNext();) {
            String element = it.next();

            if ("..".equals(element)) {
                it.remove();
                int removeCount = 1;
                while (removeCount > 0 && it.hasNext()) {
                    element = it.next();
                    if ("..".equals(element))
                        removeCount++;
                    else
                        removeCount--;
                    it.remove();
                }
            }
        }
        
        return StringUtils.join('/', elements.toArray(new String[0]));
    }

    public boolean exists(String path) {
        try (InputStream in = openRead(path)) {
            return true;
        } catch (FileNotFoundException ex) {
            return getAlternativeOutput() != null && getAlternativeOutput().exists(path);
        } catch (IOException ex) {
            return true;
        }
    }

    private static class Null extends VFilesystem {

        @Override
        public InputStream openRawRead(String path) throws IOException {
            throw new FileNotFoundException("This filesystem is empty");
        }

        @Override
        public String toString() {
            return "NULL";
        }
        
    }

    public static final VFilesystem nullFS() {
        return new Null();
    }
    
    public static final VFilesystem NULL = new Null() {

        @Override
        public void setAlternativeOutput(VFilesystem alternativeOutput) {
            throw new UnsupportedOperationException("Null");
        }

    };

    public static final VFilesystem CWD = new OSFilesystem() {

        @Override
        public File toFile(String path) {
            return new File(toRelativePath(path).replace('/', File.separatorChar));
        }

        @Override
        public String toString() {
            return ".";
        }

    };

    public static VFilesystem forFile(final File root) {
        if (root.isDirectory())
            return new OSFilesystem() {

                @Override
                public File toFile(String path) {
                    return new File(root, toRelativePath(path).replace('/', File.separatorChar));
                }

                @Override
                public String toString() {
                    return root.toString();
                }

            };

        if (root.getName().endsWith(".zip"))
            return new ZipFilesystem(root);
        
        return NULL;
    }

    public static VFilesystem getResource(final Class class_, final String rootPath) {
        final String rootRelPath = toRelativePath(rootPath);
        
        return new VFilesystem() {

            private String getFullPath(String path) {
                StringBuilder pathBuilder = new StringBuilder(path.length() + rootRelPath.length() + 2);
                pathBuilder.append('/');
                pathBuilder.append(rootRelPath);
                pathBuilder.append('/');
                pathBuilder.append(path);
                return pathBuilder.toString();
            }

            @Override
            public InputStream openRawRead(String path) throws IOException {
                path = getFullPath(path);

                InputStream in = class_.getResourceAsStream(path);
                if (in == null) {
                    if (in == null)
                        throw new FileNotFoundException("Resource \"" + path + "\" not found");
                }
                return in;
            }

            @Override
            public String toString() {
                return "resource:" + rootRelPath;
            }
            
        };
    }

    public static VFilesystem unite(final VFilesystem... vfs) {
        if (vfs.length == 0)
            throw new IllegalArgumentException();
        if (vfs.length == 1)
            return vfs[0];

        return new VFilesystem() {

            @Override
            public InputStream openRawRead(String path) throws IOException {
                IOException firstEx = null;
                int notFound = 0;
                for (VFilesystem v : vfs) {
                    try {
                        return v.openRawRead(path);
                    } catch (IOException ex) {
                        if (ex instanceof FileNotFoundException) {
                            notFound++;
                            continue;
                        }
                        if (firstEx == null)
                            firstEx = ex;
                    }
                }
                if (notFound == vfs.length)
                    throw new FileNotFoundException(path);
                throw new IOException("Union read failed for " + path, firstEx);
            }

            @Override
            public OutputStream openRawWrite(String path, boolean append) throws IOException {
                IOException firstEx = null;
                for (VFilesystem v : vfs) {
                    try {
                        return v.openRawWrite(path, append);
                    } catch (IOException ex) {
                        if (firstEx == null)
                            firstEx = ex;
                    }
                }
                throw new IOException("Union read failed for " + path, firstEx);
            }

            @Override
            public File toFile(String path) {
                return vfs[0].toFile(path);
//                for (VFilesystem vf : vfs) {
//                    File f = vf.toFile(path);
//                    if (f != null) {
//                        return f;
//                    }
//                }
//                return null;
            }

            @Override
            public boolean exists(String path) {
                if (getAlternativeOutput() != null && getAlternativeOutput().exists(path))
                    return true;
                for (VFilesystem vf : vfs) {
                    if (vf.exists(path)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean existsAndWriteable(String path) {
                if (getAlternativeOutput() != null && getAlternativeOutput().existsAndWriteable(path))
                    return true;
                for (VFilesystem vf : vfs) {
                    if (vf.existsAndWriteable(path)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                StringBuilder b = new StringBuilder();
                b.append("union:");
                for (int i = 0; i < vfs.length; i++) {
                    if (i > 0)
                        b.append('+');
                    b.append(vfs[i].toString());
                }
                return b.toString();
            }
            
        };
    }

    public boolean existsAndWriteable(String path) {
        File f = toFile(path);
        return (f == null ? false : (f.exists() && f.canWrite())) || (getAlternativeOutput() != null && getAlternativeOutput().existsAndWriteable(path));
    }

    @Override
    public String toString() {
        return getClass().toString();
    }

}
