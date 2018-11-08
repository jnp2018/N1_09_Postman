package httpRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String send(
            String requestUrl,
            String method,
            String contentType,
            String acceptContentType,
            Map<String, String> headers,
            String body
    ) throws Exception {
        requestUrl = requestUrl.replaceAll(" ", "%20");
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();

        connection.addRequestProperty("Accept-Content", acceptContentType);
        connection.addRequestProperty("Accept-Charset", DEFAULT_CHARSET);
        connection.addRequestProperty("Content-Type", String.format("%s; charset=%s", contentType, DEFAULT_CHARSET));

        for (Map.Entry<String, String> h : headers.entrySet()) {
            connection.addRequestProperty(h.getKey(), h.getValue());
        }

        connection.setFollowRedirects(true);
        // default is "GET".
        connection.setRequestMethod(method);
        if ("POST".equals(method) || "PUT".equals(method)) {
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, DEFAULT_CHARSET));
            pw.print(body);
            pw.close();
            os.close();
        }
        StringBuffer headerSection = new StringBuffer();
        for (Map.Entry<String, List<String>> e : connection
                .getHeaderFields().entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
            if (e.getKey() != null) {
                headerSection.append(e.getKey() + " = " + e.getValue()).append("\n");
            } else {
                headerSection.append(e.getValue()).append("\n");
            }
        }
        System.out.println("\n\n");
        headerSection.append("\n\n");

        String responseCharset = DEFAULT_CHARSET;
        try {
            String responseContentType = connection.getHeaderField("Content-Type");
            if (responseContentType != null) {
                int startOfEqual = responseContentType.indexOf('=');
                if (startOfEqual != -1) {
                    responseCharset = responseContentType.substring(startOfEqual + 1);
                    responseCharset = (responseCharset == null) ? DEFAULT_CHARSET
                            : responseCharset;
                } else {
                    responseCharset = DEFAULT_CHARSET;
                }
            }
        } catch (Throwable t) {
        }
        if (connection.getResponseCode() < 300
                && connection.getResponseCode() >= 200) {

            return headerSection.toString() + asString(connection.getInputStream(), responseCharset);
        } else {
            //return headerSection.toString();
            return headerSection.toString() + asString(connection.getErrorStream(), responseCharset);
        }
    }


    public static String asString(InputStream pStream, String pEncoding)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(pStream, baos, true);
        return baos.toString(pEncoding);
    }


    private static final int DEFAULT_BUFFER_SIZE = 8192;

    
    public static long copy(InputStream pInputStream,
                            OutputStream pOutputStream, boolean pClose)
            throws IOException {
        return copy(pInputStream, pOutputStream, pClose,
                new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(InputStream pIn,
                            OutputStream pOut, boolean pClose,
                            byte[] pBuffer)
            throws IOException {
        OutputStream out = pOut;
        InputStream in = pIn;
        try {
            long total = 0;
            for (; ; ) {
                int res = in.read(pBuffer);
                if (res == -1) {
                    break;
                }
                if (res > 0) {
                    total += res;
                    if (out != null) {
                        out.write(pBuffer, 0, res);
                    }
                }
            }
            if (out != null) {
                if (pClose) {
                    out.close();
                } else {
                    out.flush();
                }
                out = null;
            }
            in.close();
            in = null;
            return total;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                        
                }
            }
            if (pClose && out != null) {
                try {
                    out.close();
                } catch (Throwable t) {
	                    
                }
            }
        }
    }

}
