package com.blazingkin.interpreter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.blazingkin.interpreter.variables.Value;

@TruffleLanguage.Registration(id = "blz", name="blz", version="2.6", mimeType=BLZLanguage.MIME_TYPE)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, DebuggerTags.AlwaysHalt.class})
public class BLZLanguage extends TruffleLanguage<BLZContext> {

	public static final String MIME_TYPE = "application/x-blz";
	
	public BLZLanguage() {
		//no-op
	}
	
	@Override
	protected BLZContext createContext(Env env) {
		return new BLZContext(env, this);
	}

	@Override 
	protected Object getLanguageGlobal(BLZContext context) {
		// TODO Auto-generated method stub
		return context;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		return object instanceof Value;
	}
	
	@Override
	protected CallTarget parse(ParsingRequest request) throws Exception {
		return new BLZCallTarget(request.getSource());
	}

	public static BLZContext getCurrentBLZContext(){
		return getCurrentContext(BLZLanguage.class);
	}

}
