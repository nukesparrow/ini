/*
 * Copyright 2016 Nuke Sparrow <nukesparrow@bitmessage.ch>.
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

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IOUtils {

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteOutputStream o = new ByteOutputStream();
        byte[] buf = new byte[Math.max(4096, in.available())];
        
        for(;;) {
            int nr = in.read(buf);
            
            if (nr <= 0)
                break;
            
            o.write(buf, 0, nr);
        }
        
        return o.getBytes();
    }

    public static String toString(InputStream in, Charset charset) throws IOException {
        return new String(toByteArray(in), charset);
    }

}
