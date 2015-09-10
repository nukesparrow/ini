
import java.io.IOException;
import nukesparrow.ini.IniFile;

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

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class Examples {

    public static void readExample() throws IOException {
        // returns null for missing key
        IniFile.getIni("f.ini").get("stringKey");
        
        // returns 1234 for missing key
        IniFile.getIni("f.ini").get("integerKey", 1234);
        
        // reads from section "inisection", returns null for missing key
        IniFile.getIni("f.ini").section("inisection").get("stringKey");
        
        // reads from section "inisection", returns 1234 for missing key
        IniFile.getIni("f.ini").section("inisection").get("integerKey", 1234);
    }

    public static void writeExample() throws IOException {
        IniFile.getIni("f.ini").enableAutoSave();
        
        IniFile.getIni("f.ini").set("stringKey", "value");
        
        IniFile.getIni("f.ini").set("integerKey", 1234);
        
        // creates section "inisection" if missing
        IniFile.getIni("f.ini").section("inisection").set("stringKey", "1234");
        
        // creates section "inisection" if missing
        IniFile.getIni("f.ini").section("inisection").set("integerKey", 1234);

        // bulk modification also available
        IniFile.getIni("f.ini").disableAutoSave();

        for(int i = 0; i < 1000; i++) {
            IniFile.getIni("f.ini").section("inisection").set("integerKey#" + i, i);
        }
        
        IniFile.getIni("f.ini").saveIfModified();
    }

}
