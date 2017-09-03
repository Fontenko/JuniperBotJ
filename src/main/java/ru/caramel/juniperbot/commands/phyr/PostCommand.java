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
package ru.caramel.juniperbot.commands.phyr;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.apache.commons.lang3.StringUtils;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.caramel.juniperbot.commands.Command;
import ru.caramel.juniperbot.commands.model.DiscordCommand;
import ru.caramel.juniperbot.configuration.DiscordConfig;
import ru.caramel.juniperbot.integration.instagram.InstagramClient;
import ru.caramel.juniperbot.commands.model.BotContext;
import ru.caramel.juniperbot.integration.discord.model.DiscordException;
import ru.caramel.juniperbot.commands.model.ValidationException;
import ru.caramel.juniperbot.service.MessageService;
import ru.caramel.juniperbot.service.PostService;

import java.util.List;

@DiscordCommand(key = "discord.command.post.key", description = "discord.command.post.desc", priority = 3)
public class PostCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostCommand.class);

    @Autowired
    private InstagramClient instagramClient;

    @Autowired
    protected PostService postService;

    @Autowired
    protected MessageService messageService;

    @Override
    public boolean doCommand(MessageReceivedEvent message, BotContext context, String content) throws DiscordException {
        int count = parseCount(content);
        List<MediaFeedData> medias = null;
        try {
            medias = instagramClient.getRecent();
        } catch (InstagramException e) {
            LOGGER.error("Could not get instagram data", e);
        }

        if (medias == null) {
            messageService.onError(message.getChannel(), "discord.command.post.error");
            return false;
        }
        if (medias.isEmpty()) {
            messageService.onMessage(message.getChannel(), "discord.command.post.empty");
            return false;
        }

        if (count > medias.size()) {
            messageService.onMessage(message.getChannel(),"discord.command.post.exceed", medias.size());
            count = medias.size();
        }
        medias = medias.subList(0, count);
        postService.post(medias, message.getChannel());
        return true;
    }

    private static int parseCount(String content) throws ValidationException {
        int count = 1;
        if (StringUtils.isNotEmpty(content)) {
            try {
                count = Integer.parseInt(content);
            } catch (NumberFormatException e) {
                throw new ValidationException("discord.global.integer.parseError");
            }
            if (count == 0) {
                throw new ValidationException("discord.command.post.parse.zero");
            } else if (count > DiscordConfig.MAX_DETAILED) {
                throw new ValidationException("discord.command.post.parse.max");
            } else if (count < 0) {
                throw new ValidationException("discord.global.integer.negative");
            }
        }
        return count;
    }
}
