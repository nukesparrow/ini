# Ini File Adapter

I've written more convenient (imho) ini reader/writer, than I've found on the internet.
I don't know what to say here. Suggest something.

## Basic usage examples

```java
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
```