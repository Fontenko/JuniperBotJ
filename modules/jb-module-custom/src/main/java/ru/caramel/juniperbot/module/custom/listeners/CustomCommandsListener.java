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
package ru.caramel.juniperbot.module.custom.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;
import ru.caramel.juniperbot.core.persistence.entity.GuildConfig;
import ru.caramel.juniperbot.core.service.*;
import ru.caramel.juniperbot.core.utils.CommonUtils;
import ru.caramel.juniperbot.core.utils.MapPlaceholderResolver;
import ru.caramel.juniperbot.module.custom.persistence.entity.CustomCommand;
import ru.caramel.juniperbot.module.custom.persistence.repository.CustomCommandRepository;

import javax.annotation.PostConstruct;
import java.util.regex.Pattern;

@Component
public class CustomCommandsListener implements CommandSender, CommandHandler {

    private static PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("{", "}");

    @Autowired
    private CommandsService commandsService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CustomCommandRepository commandRepository;

    @Autowired
    private ConfigService configService;

    @PostConstruct
    public void init() {
        commandsService.registerHandler(this);
    }

    @Override
    public boolean handleMessage(MessageReceivedEvent event) {
        return commandsService.sendMessage(event, this, e -> isAnyCustomCommand(event, e));
    }

    public boolean isAnyCustomCommand(MessageReceivedEvent event, String input) {
        if (event.getGuild() == null) {
            return false;
        }
        String prefix = configService.getPrefix(event.getGuild().getIdLong());
        if (StringUtils.isEmpty(prefix)) {
            return false;
        }
        if (!input.startsWith(prefix)) {
            return false;
        }
        String key = input.replaceFirst("^" + Pattern.quote(prefix), "");
        return commandRepository.existsByKeyAndConfigGuildId(key, event.getGuild().getIdLong());
    }

    public boolean sendCommand(MessageReceivedEvent event, String content, String key, GuildConfig config) {
        CustomCommand command = commandRepository.findByKeyAndConfig(key, config);
        if (command == null) {
            return false;
        }
        String commandContent = placeholderHelper.replacePlaceholders(command.getContent(), getResolver(event, content));
        switch (command.getType()) {
            case ALIAS:
                String[] args = commandContent.split("\\s+");
                if (args.length > 0) {
                    commandContent = commandContent.substring(args[0].length(), commandContent.length()).trim();
                    return commandsService.sendCommand(event, CommonUtils.trimTo(commandContent, 2000), args[0], config);
                }
                break;
            case MESSAGE:
                messageService.sendMessageSilent(event.getChannel()::sendMessage,
                        CommonUtils.trimTo(commandContent, 2000));
                return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    private MapPlaceholderResolver getResolver(MessageReceivedEvent event, String content) {
        MapPlaceholderResolver resolver = new MapPlaceholderResolver();
        resolver.put("author", event.getAuthor().getAsMention());
        resolver.put("guild", event.getGuild().getName());
        resolver.put("content", content);
        return resolver;
    }
}
