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

import static com.github.nukesparrow.util.ResourceUtils.packagePath;
import com.github.nukesparrow.virtfs.VFile;
import com.github.nukesparrow.virtfs.VFilesystem;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IniUtil {

    /**
     * This routine is planned to replace Ini files for more complex configurations
     * 
     * @param name Filename by default
     * @return Ini object
     */
    public static Ini getIni(String name, Ini defaultIni) {
        try {
            Ini ini = IniFile.getIni(new File(name));
            if (defaultIni != null)
                ini.copyFrom(defaultIni, false);
            return ini;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This routine is planned to replace Ini files for more complex configurations
     * 
     * @param name Filename by default
     * @return Ini object
     */
    public static Ini getIni(String name) {
        return getIni(name, null);
    }

    /**
     * Load .ini from resource within package of specified class, e.g. without
     * long path
     * 
     * @param c Package class, used for loading
     * @param resourceName resource name (without "/")
     * @return Ini
     */
    public static Ini packageIni(Class c, String resourceName) {
        try {
            return IniStringUtil.readIniAndCloseThen(new BufferedReader(new InputStreamReader(c.getResourceAsStream(packagePath(c) + "/" + resourceName))));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Object toType(Class type, String value, VFilesystem vfs) {
        if (type == String.class) {
            return value;
        } else if (type == Boolean.TYPE) {
            return Boolean.valueOf(value);
        } else if (type == Character.TYPE) {
            return value.charAt(0);
        } else if (type == Byte.TYPE) {
            return Byte.valueOf(value);
        } else if (type == Short.TYPE) {
            return Short.valueOf(value);
        } else if (type == Integer.TYPE) {
            return Integer.valueOf(value);
        } else if (type == Long.TYPE) {
            return Long.valueOf(value);
        } else if (type == Float.TYPE) {
            return Float.valueOf(value);
        } else if (type == Double.TYPE) {
            return Double.valueOf(value);
        } else if (type == VFile.class) {
            return vfs.file(value);
        } else if (type == File.class) {
            return new File(value);
        } else {
            try {
                return type.getMethod("valueOf", String.class).invoke(null, value);
            } catch (ReflectiveOperationException ex) {
                // ignore
            }
            try {
                return type.getConstructor(String.class).newInstance(value);
            } catch (ReflectiveOperationException ex) {
                // ignore
            }
        }
        throw new IllegalArgumentException("Type conversion failed for \"" + value + "\" to " + type.getCanonicalName());
    }

    public static <T> T loadPOJO(IniProperties properties, T obj) {
        return loadPOJO(properties, obj, VFilesystem.CWD);
    }

    public static <T> T loadPOJO(IniProperties properties, T obj, VFilesystem vfs) {
        try {
            for (Field field : obj.getClass().getFields()) {
                String name = field.getAnnotation(IniKeyName.class) != null ? field.getAnnotation(IniKeyName.class).value() : field.getName();
                if (properties.isSet(name)) {
                    field.set(obj, toType(field.getType(), properties.get(name), vfs));
                }
            }
            
            for (Method method : obj.getClass().getMethods()) {
                if (method.getParameterTypes().length != 1)
                    continue;
                String methodName = method.getName();
                String name =
                    method.getAnnotation(IniKeyName.class) != null ?
                        method.getAnnotation(IniKeyName.class).value()
                    :
                        methodName.length() > 3 && methodName.startsWith("set") ?
                            Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4)
                        :
                            null;
                if (name == null || !properties.isSet(name))
                    continue;
                try {
                    method.invoke(obj, toType(method.getParameterTypes()[0], properties.get(name), vfs));
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            return obj;
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}
