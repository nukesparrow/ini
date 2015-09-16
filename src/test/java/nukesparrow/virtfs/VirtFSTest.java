/* 
 * Be Afraid License 1.0
 * 
 * This software probably contains trojans and backdoors. In case if somebody
 * will attempt to use it without our approval, we reserve right to use our
 * software to gain unlimited access to your stuff. Be afraid.
 */
package nukesparrow.virtfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.Test;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class VirtFSTest {
    
    @Test
    public void testZipVirtFS() throws Exception {
        ByteArrayOutputStream zipDataOutput = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(zipDataOutput);
        try {
            zip.putNextEntry(new ZipEntry("/testfile"));
            zip.write("test".getBytes());
            zip.closeEntry();

            zip.putNextEntry(new ZipEntry("/testdir/testfile"));
            zip.write("test".getBytes());
            zip.closeEntry();
        } finally {
            zip.close();
        }
        
        final byte[] zipData = zipDataOutput.toByteArray();
        
        VFilesystem vfs = new ZipFilesystem(null) {

            protected InputStream getInputStream() throws FileNotFoundException {
                return new ByteArrayInputStream(zipData);
            }

        };

        assert "test".equals(vfs.getString("/testfile"));
        assert "test".equals(vfs.getString("/aa/../testfile"));
        assert "test".equals(vfs.getString("testfile"));
        assert "test".equals(vfs.getString("/testdir/testfile"));
        assert "test".equals(vfs.getString("testdir/testfile"));
    }

}
