package com.zzzhc.curl;

/**
 * see <a href="http://curl.haxx.se/libcurl/c/curl_easy_getinfo.html">
 * curl_easy_getinfo</a>
 * 
 * <pre>
 * TIMES
 * 
 * An overview of the six time values available from curl_easy_getinfo()
 * 
 * curl_easy_perform()   |   |--NAMELOOKUP   |--|--CONNECT   |--|--|--APPCONNECT   |--|--|--|--PRETRANSFER   |--|--|--|--|--STARTTRANSFER   |--|--|--|--|--|--TOTAL   |--|--|--|--|--|--REDIRECT
 * 
 * NAMELOOKUP
 * 
 * CURLINFO_NAMELOOKUP_TIME. The time it took from the start until the name resolving was completed.
 * 
 * CONNECT
 * 
 * CURLINFO_CONNECT_TIME. The time it took from the start until the connect to the remote host (or proxy) was completed.
 * 
 * APPCONNECT
 * 
 * CURLINFO_APPCONNECT_TIME. The time it took from the start until the SSL connect/handshake with the remote host was completed. (Added in in 7.19.0)
 * 
 * PRETRANSFER
 * 
 * CURLINFO_PRETRANSFER_TIME. The time it took from the start until the file transfer is just about to begin. This includes all pre-transfer commands and negotiations that are specific to the particular protocol(s) involved.
 * 
 * STARTTRANSFER
 * 
 * CURLINFO_STARTTRANSFER_TIME. The time it took from the start until the first byte is received by libcurl.
 * 
 * TOTAL
 * 
 * CURLINFO_TOTAL_TIME. Total time of the previous request.
 * 
 * REDIRECT
 * 
 * CURLINFO_REDIRECT_TIME. The time it took for all redirection steps include name lookup, connect, pretransfer and transfer before final transaction was started. So, this is zero if no redirection took place.
 * </pre>
 */
public class Info {

	public String url;
	public String contentType;
	public int httpCode;

	public String primaryIp;
	public int primaryPort;
	public String localIp;
	public int localPort;

	public double totalTime;
	public double namelookupTime;
	public double connectTime;
	public double appconnectTime;
	public double pretransferTime;
	public double starttransferTime;
	public double redirectTime;

	public int redirectCount;
	public String redirectUrl;

	public double sizeUpload;
	public double sizeDownload;
	public double speedDownload;
	public double speedUpload;

	public long headerSize;
	public long requestSize;
	public long sslVerifyresult;
	public long filetime;

	public double contentLengthDownload;
	public double contentLengthUpload;

	@Override
	public String toString() {
		return "Info [url=" + url + ", contentType=" + contentType
				+ ", httpCode=" + httpCode + ", primaryIp=" + primaryIp
				+ ", primaryPort=" + primaryPort + ", localIp=" + localIp
				+ ", localPort=" + localPort + ", totalTime=" + totalTime
				+ ", namelookupTime=" + namelookupTime + ", connectTime="
				+ connectTime + ", appconnectTime=" + appconnectTime
				+ ", pretransferTime=" + pretransferTime
				+ ", starttransferTime=" + starttransferTime
				+ ", redirectTime=" + redirectTime + ", redirectCount="
				+ redirectCount + ", redirectUrl=" + redirectUrl
				+ ", sizeUpload=" + sizeUpload + ", sizeDownload="
				+ sizeDownload + ", speedDownload=" + speedDownload
				+ ", speedUpload=" + speedUpload + ", headerSize=" + headerSize
				+ ", requestSize=" + requestSize + ", sslVerifyresult="
				+ sslVerifyresult + ", filetime=" + filetime
				+ ", contentLengthDownload=" + contentLengthDownload
				+ ", contentLengthUpload=" + contentLengthUpload + "]";
	}

}
