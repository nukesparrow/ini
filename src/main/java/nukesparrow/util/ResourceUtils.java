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
package nukesparrow.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class ResourceUtils {
    
    public static String packagePath(Package p) {
        return "/" + p.getName().replaceAll("\\.", "/");
    }

    public static String packagePath(Class c) {
        return packagePath(c.getPackage());
    }

    public static InputStream getPackageResource(Class c, String name) {
        return c.getResourceAsStream(packagePath(c) + "/" + name);
    }

    public static final Charset IOCHARSET = Charset.forName("UTF-8");

    public static byte[] getPackageResourceAsByteArray(Class c, String name) {
        try (InputStream in = getPackageResource(c, name)) {
            return IOUtils.toByteArray(in);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getPackageResourceAsString(Class c, String name) {
        try (InputStream in = getPackageResource(c, name)) {
            return IOUtils.toString(in, IOCHARSET);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
