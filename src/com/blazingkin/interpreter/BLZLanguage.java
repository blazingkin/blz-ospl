package com.blazingkin.interpreter;

import com.oracle.truffle.api.TruffleLanguage;

@TruffleLanguage.Registration(id = "blz", name="blz", version="2.6", mimeType=BLZLanguage.MIME_TYPE)
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

}
