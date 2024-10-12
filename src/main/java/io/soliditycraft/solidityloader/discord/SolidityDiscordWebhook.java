package io.soliditycraft.solidityloader.discord;

import lombok.Builder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A manager for interacting with Discord webhooks, allowing the loader to send messages and embeds to a Discord channel.
 * This class provides methods to send simple messages and complex embeds using the Embed builder class.
 */
public class SolidityDiscordWebhook {

    private final String webhookUrl;

    /**
     * Constructs a new DiscordWebhookManager with the provided webhook URL.
     *
     * @param webhookUrl The URL of the Discord webhook.
     */
    public SolidityDiscordWebhook(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    /**
     * Sends a simple text message to the Discord webhook.
     *
     * @param message The content of the message to be sent.
     * @throws Exception If there is an error sending the message.
     */
    public void sendMessage(String message) throws Exception {
        JSONObject json = new JSONObject();
        json.put("content", message);
        sendRequest(json.toString());
    }

    /**
     * Sends an embed to the Discord webhook.
     *
     * @param embed The embed object to send.
     * @throws Exception If there is an error sending the embed.
     */
    public void sendEmbed(Embed embed) throws Exception {
        JSONArray embedsArray = new JSONArray();
        embedsArray.put(embed.toJson());

        JSONObject json = new JSONObject();
        json.put("embeds", embedsArray);

        sendRequest(json.toString());
    }

    /**
     * Sends the request to the Discord webhook.
     *
     * @param jsonPayload The JSON payload to be sent.
     * @throws Exception If there is an error sending the request.
     */
    private void sendRequest(String jsonPayload) throws Exception {
        URL url = new URL(webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 204) { // 204 No Content is the expected successful response
            throw new Exception("Failed to send message. Response code: " + responseCode);
        }
    }

    /**
     * A class representing a Discord Embed message, constructed via a builder pattern.
     */
    @Builder
    public static class Embed {
        private String title;
        private String description;
        private String url;
        private int color;
        private List<Field> fields;

        /**
         * Converts the embed to a JSON object suitable for sending to Discord.
         *
         * @return A JSONObject representing the embed.
         */
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("title", title);
            json.put("description", description);
            if (url != null) {
                json.put("url", url);
            }
            json.put("color", color);

            if (!fields.isEmpty()) {
                JSONArray jsonFields = new JSONArray();
                for (Field field : fields) {
                    jsonFields.put(field.toJson());
                }
                json.put("fields", jsonFields);
            }
            return json;
        }

        /**
         * A class representing a field in a Discord embed.
         */
        public static class Field {
            private final String name;
            private final String value;
            private final boolean inline;

            public Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            /**
             * Converts the field to a JSON object suitable for adding to an embed.
             *
             * @return A JSONObject representing the field.
             */
            public JSONObject toJson() {
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("value", value);
                json.put("inline", inline);
                return json;
            }
        }
    }
}
