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
package nukesparrow.virtfs;

import nukesparrow.ini.Ini;
import nukesparrow.ini.IniUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
abstract class OSFilesystem extends VFilesystem {

    @Override
    public Ini getIni(String path) throws IOException {
        return IniUtil.getIni(toFile(path).getPath());
    }

    @Override
    public InputStream openRawRead(String path) throws IOException {
        return new FileInputStream(toFile(path));
    }

    @Override
    public OutputStream openRawWrite(String path, boolean append) throws IOException {
        File f = toFile(path);
        
        if (f == null)
            throw new IOException("Unable to detect OS file path");
        
        if (path.endsWith(".gz")) {
            if (f.exists() && append)
                throw new IOException("Unable to append to GZip stream");
            return new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(f, false))) {
                {
                    def.setLevel(Deflater.BEST_COMPRESSION);
                }
            };
        }
        return new BufferedOutputStream(new FileOutputStream(f, append));
    }

    @Override
    public abstract File toFile(String path);

    @Override
    public boolean exists(String path) {
        return toFile(path).exists() || (getAlternativeOutput() != null && getAlternativeOutput().exists(path));
    }

    @Override
    public boolean existsAndWriteable(String path) {
        if (path.endsWith(".gz"))
            return false || (getAlternativeOutput() != null && getAlternativeOutput().existsAndWriteable(path));
        File f = toFile(path);
        return (f.exists() && f.canWrite()) || (getAlternativeOutput() != null && getAlternativeOutput().existsAndWriteable(path));
    }

}
