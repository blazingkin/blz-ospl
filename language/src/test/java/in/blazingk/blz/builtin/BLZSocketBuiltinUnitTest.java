package in.blazingk.blz.builtin;

import java.io.IOException;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.socket.SocketBuiltin;
import com.blazingkin.interpreter.variables.BLZSocket;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

import org.junit.Test;

import in.blazingk.blz.UnitTestUtil;

public class BLZSocketBuiltinUnitTest {


    @Test
    public void testWithInvalidCommand() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("invalid")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Did not know how to handle mode invalid in socket built in");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testCreateWithNotEnoughArguments() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("create")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Socket Create expects three arguments, \"create\", host_name, and port");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testCreateWithInvalidTypes() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("create"), Value.string("a"), Value.string("b")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Socket Create recieved wrong argument types, expects host_name as string and port as integer");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testAcceptWithNotEnoughArguments() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("accept")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Socket Accept expects two arguments, \"accept\", socket");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testAcceptWithIncorrectTypes() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("accept"), Value.string("b")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Socket Accept expects a socket object as the second type");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testCloseWithNotEnoughArguments() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("close")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Close Socket must have 2 arguments, \"close\", and the socket");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testCloseWithIncorrectTypes() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("close"), Value.string("b")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Close Socket expects a socket");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testGetResourceWithNotEnoughArguments() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("get_resource")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Resource From Socket must have 2 arguments, \"get_resource\", and the socket");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testGetResourceWithIncorrectTypes() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("get_resource"), Value.string("b")};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Resource From Socket expects a socket");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testCreateValidClientSocket() throws IOException {
        SocketBuiltin b = new SocketBuiltin();
        try {
            Value[] args = {Value.string("create"), Value.string("example.com"), Value.integer(80)};
            Value result = b.run(args);
            UnitTestUtil.assertEqual(result.type, VariableTypes.Socket);
            BLZSocket sock = (BLZSocket) result.value;
            UnitTestUtil.assertEqual(sock.type, BLZSocket.SocketType.Client);
            sock.socket.close();
        }catch (BLZRuntimeException e){
            UnitTestUtil.fail("Expected to validly create a client socket");
        }
    }

    @Test
    public void testCreateWithInvalidHost() {
        SocketBuiltin b = new SocketBuiltin();
        boolean exceptionThrown = false;
        try {
            Value[] args = {Value.string("create"), Value.string("a"), Value.integer(3000)};
            b.run(args);
        }catch (BLZRuntimeException e){
            exceptionThrown = true;
            UnitTestUtil.assertEqual(e.getMessage(), "Unexpected socket IO exception a");
        }
        if (!exceptionThrown) {
            UnitTestUtil.fail("An exception should have been thrown");
        }
    }

    @Test
    public void testCreateValidClientSocketAndGetResourcesAndClose() throws IOException {
        SocketBuiltin b = new SocketBuiltin();
        try {
            Value[] args = {Value.string("create"), Value.string("example.com"), Value.integer(80)};
            Value result = b.run(args);
            UnitTestUtil.assertEqual(result.type, VariableTypes.Socket);
            BLZSocket sock = (BLZSocket) result.value;
            UnitTestUtil.assertEqual(sock.type, BLZSocket.SocketType.Client);
            Value[] args2 = {Value.string("get_resource"), result};
            Value res = b.run(args2);
            UnitTestUtil.assertEqual(res.type, VariableTypes.Resource);
            Value[] args3 = {Value.string("close"), result};
            b.run(args3);
        }catch (BLZRuntimeException e){
            UnitTestUtil.fail("Expected to validly create a client socket");
        }
    }




}