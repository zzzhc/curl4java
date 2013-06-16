package com.zzzhc.curl;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface CurlEasyLib extends Library {

	interface DataHandler extends Callback {
		int invoke(Pointer contents, int size, int nmemb, Pointer userp);
	}

	CurlEasyLib INSTANCE = (CurlEasyLib) Native.loadLibrary("curl",
			CurlEasyLib.class);

	String curl_version();

	Pointer curl_easy_init();

	void curl_easy_reset(Pointer curl);

	Pointer curl_easy_duphandle(Pointer curl);

	void curl_easy_cleanup(Pointer curl);

	int curl_easy_setopt(Pointer curl, int option, Object obj);

	int curl_easy_getinfo(Pointer curl, int info, Object... args);

	int curl_easy_perform(Pointer curl);

	String curl_easy_strerror(int errorNum);

	curl_slist.ByReference curl_slist_append(curl_slist.ByReference slist, String header);

	void curl_slist_free_all(curl_slist.ByReference slist);
	
	void curl_free(Pointer p);

}
