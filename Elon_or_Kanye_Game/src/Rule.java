import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Rule {
    // get tweets based on the user id
    public List<Twitter> getTwitters(long userId) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        try {

            URL url = new URL("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=" + userId + "&count=3200");
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            connection.setRequestProperty("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAALp5HwEAAAAAbJ8BBRF5kqivjgYENeLl8XChWSA%3D5TOdFPwWtTm1oHbfo5BiLztobDEzd8dlvRCFd2AcXNqjRVleek");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));


            String line = null;

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }


        return filterTwitter(userId, result.toString());
    }

    // filter the tweets based on requirements
    private List<Twitter> filterTwitter(long userId, String result) {
        List<Twitter> twitters = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // check if it is reply
                String replyStatus = jsonObject.getString("in_reply_to_status_id");
                if (!replyStatus.equals("null")) {
                    continue;
                }

                // check if it is retweet
                try {
                    jsonObject.getJSONObject("retweeted_status");
                    continue;
                } catch (JSONException jsonException) {
                }


                Twitter twitter = new Twitter();
                String text = jsonObject.getString("text");
                // check it if contains link or tag
                if (text.contains("http") || text.contains("@")) {
                    continue;
                }

                twitter.setUserId(userId);
                twitter.setText(text);

                // add the tweet to list
                twitters.add(twitter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return twitters;
    }
}
