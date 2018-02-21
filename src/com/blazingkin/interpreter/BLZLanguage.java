package com.blazingkin.interpreter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;

@TruffleLanguage.Registration(id = "blz", name="blz", version="2.6", mimeType=BLZLanguage.MIME_TYPE)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, DebuggerTags.AlwaysHalt.class})
public class BLZLanguage extends TruffleLanguage {

	public static final String MIME_TYPE = "application/x-blz";
	
	@Override
	protected Object createContext(Env env) {
		return "Hi ;)";
	}

	@Override
	protected Object getLanguageGlobal(Object context) {
		// TODO Auto-generated method stub
		return ";)";
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected CallTarget parse(ParsingRequest request) throws Exception {
		return null;
	}

}
