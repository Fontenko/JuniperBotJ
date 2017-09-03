/*
 * This file is part of JuniperBotJ.
 *
 * JuniperBotJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JuniperBotJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JuniperBotJ. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.caramel.juniperbot.integration.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.caramel.juniperbot.configuration.YouTubeConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class YouTubeClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeClient.class);

    @Autowired
    private YouTubeConfig config;

    private YouTube youTube;

    @PostConstruct
    public void init() {
        try {
            youTube = new YouTube
                    .Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), e -> {})
                    .setApplicationName(YouTubeClient.class.getSimpleName())
                    .build();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public List<SearchResult> search(String queryTerm, long maxResults)  {
        try {
            YouTube.Search.List search = youTube.search().list("id,snippet");
            search.setKey(config.getApiKey());
            search.setQ(queryTerm);
            search.setType("video");
            search.setFields("items(id/videoId, snippet/title)");
            search.setMaxResults(maxResults);
            return search.execute().getItems();
        } catch (IOException e) {
            LOGGER.error("Could not perform YouTube search", e);
        }
        return Collections.emptyList();
    }

    public List<Video> searchDetailed(String queryTerm, long maxResults)  {
        try {
            List<SearchResult> results = search(queryTerm, maxResults);
            YouTube.Videos.List list = youTube.videos().list("id,snippet,contentDetails");
            list.setKey(config.getApiKey());
            list.setId(results.stream()
                    .filter(e -> e.getId() != null && e.getId().getVideoId() != null)
                    .map(e -> e.getId().getVideoId()).collect(Collectors.joining(",")));
            return list.execute().getItems();
        } catch (IOException e) {
            LOGGER.error("Could not perform YouTube search", e);
        }
        return Collections.emptyList();
    }

    public String searchForUrl(String queryTerm) {
        List<SearchResult> result = search(queryTerm, 1L);
        return result.isEmpty() ? null : getUrl(result.get(0));
    }

    public String getUrl(SearchResult result) {
        return String.format("https://www.youtube.com/watch?v=%s", result.getId().getVideoId());
    }

    public String getUrl(Video result) {
        return String.format("https://www.youtube.com/watch?v=%s", result.getId());
    }
}
