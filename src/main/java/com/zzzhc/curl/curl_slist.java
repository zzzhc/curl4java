package com.zzzhc.curl;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class curl_slist extends Structure {
	// / C type : char*
	public Pointer data;
	// / C type : curl_slist*
	public curl_slist.ByReference next;

	public curl_slist() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("data", "next");
	}

	public curl_slist(Pointer data, curl_slist.ByReference next) {
		super();
		this.data = data;
		this.next = next;
	}

	public static class ByReference extends curl_slist implements
			Structure.ByReference {

	};

	public static class ByValue extends curl_slist implements Structure.ByValue {

	};
}
