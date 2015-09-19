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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class VFile {

    public final VFilesystem filesystem;
    public final String path;

    public VFile(VFilesystem filesystem, String path) {
        this.filesystem = filesystem;
        this.path = VFilesystem.toRelativePath(path);
    }

    public InputStream openRead() throws IOException {
        return filesystem.openRead(path);
    }

    public OutputStream openWrite(boolean append) throws IOException {
        return filesystem.openWrite(path, append);
    }

    public String getString() throws IOException {
        return filesystem.getString(path);
    }

    public byte[] getBytes() throws IOException {
        return filesystem.getBytes(path);
    }

    public Ini getIni() throws IOException {
        return filesystem.getIni(path);
    }

    public File toFile() {
        return filesystem.toFile(path);
    }

    public boolean exists() {
        return filesystem.exists(path);
    }

    public boolean existsAndWriteable() {
        return filesystem.existsAndWriteable(path);
    }

    @Override
    public String toString() {
        return path;
    }

}
