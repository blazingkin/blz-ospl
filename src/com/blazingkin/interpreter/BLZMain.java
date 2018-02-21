package com.blazingkin.interpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class BLZMain {

	public static void main(String[] args) throws IOException {
		Source source;
		if (args.length == 0) {
			source = Source.newBuilder(new InputStreamReader(System.in)).
					name("<stdin>").
					mimeType(BLZLanguage.MIME_TYPE).
					build();
		} else {
			source = Source.newBuilder(new File(args[0])).build();
		}
		run(source, System.in, System.out);
	}
	
	private static void run(Source source, InputStream in, PrintStream out) {
		out.println("== running on " + Truffle.getRuntime().getName());
		PolyglotEngine engine = PolyglotEngine.newBuilder().setIn(in).setOut(out).build();
		assert engine.getLanguages().containsKey(BLZLanguage.MIME_TYPE);
		engine.eval(source);
		engine.dispose();
	}
	
}
