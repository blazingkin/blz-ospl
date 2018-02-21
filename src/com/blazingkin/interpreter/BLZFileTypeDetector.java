package com.blazingkin.interpreter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

public final class BLZFileTypeDetector extends FileTypeDetector {
public static final String asdf = "adsf";
	
	@Override
	public String probeContentType(Path path) throws IOException {
		if (path.toFile().toString().endsWith(".blz")) {
			return BLZLanguage.MIME_TYPE;
		}
		return null;
	}

}
