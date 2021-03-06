package com.kitteless.kittelessback.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitteless.kittelessback.model.ClovaOCRResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

@Repository
public class OcrRepository {

    public ClovaOCRResponse read(String base64EncodedImage) {
        try {
            String response = doPost(base64EncodedImage);

            ObjectMapper mapper = new ObjectMapper();
            ClovaOCRResponse clovaOCRResponse = mapper.readValue(response, ClovaOCRResponse.class);

            return clovaOCRResponse;
        } catch (Exception e) {
            // 握りつぶす
        }
        return null;
    }

    private String doPost(String base64) {
        final String CLOVA_OCR_CUSTOM_API = "https://bef2c44061654479868834a873039292.apigw.ntruss.com/custom/v1/464/2580fcb43bc90fe421f10c7206a0f8ae6fd43667eaeda7c4d679e21910b0b1e9/general";
        final String CLOVA_OCR_CUSTOM_SECRET = "Q05vcnJzTUd0eE1mYVFVWkpDVHhNclFzcWR5T1p3VnM=";

        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(CLOVA_OCR_CUSTOM_API);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", CLOVA_OCR_CUSTOM_SECRET);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            json.put("lang", "ja");
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            writeMultiPart(wr, postParams, base64, boundary);
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
        } catch (Exception e) {
            // 握りつぶす
        }

        return response.toString();
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, String base64, String boundary)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (base64 != null) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + "demo" + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();


            byte[] bytes = Base64.getDecoder().decode(base64);
            out.write(bytes);
            out.write("\r\n".getBytes());

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }
}
