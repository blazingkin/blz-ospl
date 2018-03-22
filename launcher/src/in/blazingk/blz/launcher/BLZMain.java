package in.blazingk.blz.launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public final class BLZMain {

    private static final String BLZ = "blz";

    /**
     * The main entry point.
     */
    public static void main(String[] args) throws IOException {
        Source source;
        Map<String, String> options = new HashMap<String, String>();
        String file = null;
        for (String arg : args) {
            if (parseOption(options, arg)) {
                continue;
            } else {
                if (file == null) {
                    file = arg;
                }
            }
        }

        if (file == null) {
            // @formatter:off
            source = Source.newBuilder(BLZ, new InputStreamReader(System.in), "<stdin>").build();
            // @formatter:on
        } else {
            source = Source.newBuilder(BLZ, new File(file)).build();
        }

        System.exit(executeSource(source, System.in, System.out, options));
    } 

    private static int executeSource(Source source, InputStream in, PrintStream out, Map<String, String> options) {
        Context context;
        try {
            context = Context.newBuilder(BLZ).in(in).out(out).options(options).build();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return 1;
        }
        out.println("== running on " + context.getEngine());
        out.println(context.getEngine().getLanguages());
        try {
            Value result = context.eval(source);
            if (context.lookup(BLZ, "main") == null) {
                System.err.println("No function main defined in blz source file.");
                return 1;
            }
            if (!result.isNull()) {
                out.println(result.toString());
            }
            return 0;
        } catch (PolyglotException ex) {
            if (ex.isInternalError()) {
                // for internal errors we print the full stack trace
                ex.printStackTrace();
            } else {
                System.err.println(ex.getMessage());
            }
            return 1;
        } finally {
            context.close();
        }
    }

    private static boolean parseOption(Map<String, String> options, String arg) {
        if (arg.length() <= 2 || !arg.startsWith("--")) {
            return false;
        }
        int eqIdx = arg.indexOf('=');
        String key;
        String value;
        if (eqIdx < 0) {
            key = arg.substring(2);
            value = null;
        } else {
            key = arg.substring(2, eqIdx);
            value = arg.substring(eqIdx + 1);
        }

        if (value == null) {
            value = "true";
        }
        int index = key.indexOf('.');
        String group = key;
        if (index >= 0) {
            group = group.substring(0, index);
        }
        options.put(key, value);
        return true;
    }

}
