package in.blazingk.blz;

import java.io.File;
import java.io.IOException;

import com.blazingkin.interpreter.variables.BLZResource;

import org.junit.Test;

public class BLZResourceUnitTest {


    @Test
    public void testResource() throws IOException {
        BLZResource resource = new BLZResource(File.createTempFile("blah", "outran").toURI());
        resource.open(BLZResource.FileMode.Create);
        resource.open(BLZResource.FileMode.Write);
        resource.write("TEST!\nAnotherThing\n");
        resource.close();
        resource.open(BLZResource.FileMode.Read);
        UnitTestUtil.assertEqual(resource.read(), "T");
        UnitTestUtil.assertEqual(resource.read(), "E");
        resource.close();
    }

}