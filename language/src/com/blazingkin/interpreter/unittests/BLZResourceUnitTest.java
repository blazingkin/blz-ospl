package com.blazingkin.interpreter.unittests;

import java.io.File;
import java.io.IOException;

import com.blazingkin.interpreter.variables.BLZResource;

import org.junit.Test;

public class BLZResourceUnitTest {


    @Test
    public void testResource() throws IOException {
        BLZResource resource = new BLZResource(new File("/tmp/testfile2").toURI());
        resource.open(BLZResource.FileMode.Create);
        resource.open(BLZResource.FileMode.Write);
        resource.write("TEST!\nAnotherThing\n");
        resource.close();
        resource.open(BLZResource.FileMode.Read);
        System.out.println(resource.read());
        System.out.println(resource.read());
        resource.close();
    }

}