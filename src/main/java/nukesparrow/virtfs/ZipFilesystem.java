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

import static nukesparrow.virtfs.VFilesystem.toRelativePath;
import nukesparrow.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
class ZipFilesystem extends VFilesystem {

    public final File zipFile;

    public ZipFilesystem(File zipFile) {
        this.zipFile = zipFile;
    }

    protected InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(zipFile);
    }

    @Override
    public InputStream openRawRead(String path) throws IOException {
        if (path == null || path.isEmpty())
            throw new FileNotFoundException();

        ZipInputStream in = new ZipInputStream(getInputStream());
        try {
            path = toRelativePath(path);

            ZipEntry ze;

            while ((ze = in.getNextEntry()) != null) {
                String entryPath = toRelativePath(ze.getName());

                if (entryPath.equals(path)) {
                    byte[] data = StreamUtils.streamToByteArray(in, false);
                    return new ByteArrayInputStream(data);
                }
            }

            if (path == null)
                throw new FileNotFoundException("File \"" + path + "\" not found in " + zipFile);

            throw new FileNotFoundException(path + " not found");
        } finally {
            in.close();
        }
    }

    @Override
    public String toString() {
        return zipFile.toString();
    }

}
