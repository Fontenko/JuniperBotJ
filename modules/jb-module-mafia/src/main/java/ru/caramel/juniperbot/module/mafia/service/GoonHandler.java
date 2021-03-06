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
package ru.caramel.juniperbot.module.mafia.service;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.caramel.juniperbot.module.mafia.model.*;
import ru.caramel.juniperbot.module.mafia.service.base.ChoiceStateHandler;
import ru.caramel.juniperbot.module.mafia.service.individual.BrokerHandler;

import java.util.*;

@Component
public class GoonHandler extends ChoiceStateHandler {

    @Autowired
    private BrokerHandler brokerHandler;

    @Override
    public boolean onStart(User user, MafiaInstance instance) {
        instance.setState(MafiaState.NIGHT_GOON);

        MafiaPlayer exiledPlayer = instance.getDailyActions().get(MafiaActionType.EXILE);

        boolean endOfGame = false;
        StringBuilder stringBuilder = new StringBuilder();
        if (exiledPlayer != null) {
            outPlayer(instance, exiledPlayer);
            String roleName = messageService.getEnumTitle(exiledPlayer.getRole());
            stringBuilder.append(messageService.getMessage("mafia.night.start.exiled", roleName,
                    exiledPlayer.getName()));

            boolean hasAnyMafia = instance.hasAnyMafia();
            boolean hasAnyTownie = instance.hasAnyTownie();
            if (!hasAnyMafia && !hasAnyTownie) {
                stringBuilder.append("\n\n").append(messageService.getMessage("mafia.end.standoff"));
                endOfGame = true;
            } else if (!hasAnyMafia) {
                stringBuilder.append("\n\n").append(messageService.getMessage("mafia.end.townies-wins"));
                endOfGame = true;
            } else if (!hasAnyTownie) {
                stringBuilder.append("\n\n").append(messageService.getMessage("mafia.end.mafia-wins"));
                endOfGame = true;
            }
        }

        if (!endOfGame) {
            stringBuilder.append("\n\n").append(messageService.getMessage("mafia.night.start"));
        }

        EmbedBuilder embedBuilder = getBaseEmbed();
        embedBuilder.setDescription(stringBuilder.toString());
        instance.getChannel().sendMessage(embedBuilder.build()).complete();
        if (endOfGame) {
            instance.setIgnoredReason();
            return true;
        }

        if (instance.getGoons().isEmpty()) {
            return brokerHandler.onStart(user, instance);
        }

        List<MafiaPlayer> players = new ArrayList<>(instance.getAlive());
        MessageBuilder builder = new MessageBuilder();
        EmbedBuilder embed = getBaseEmbed("mafia.goon.choice");
        embed.addField(messageService.getMessage("mafia.start.playerList.title"),
                getPlayerList(players), false);
        embed.setFooter(messageService.getMessage("mafia.goon.choice.footer",
                getEndTimeText(instance, dayDelay), instance.getPrefix()), null);
        builder.setEmbed(embed.build());
        builder.setContent(instance.getGoonsMentions());

        Message message = instance.getGoonChannel().sendMessage(builder.build()).complete();

        sendChoice(instance, message, instance.getGoons());

        return scheduleEnd(instance, dayDelay);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onEnd(User user, MafiaInstance instance) {
        if (user != null && !instance.isPlayer(user, MafiaRole.GOON)) {
            return false;
        }
        MafiaPlayer toKill = getChoiceResult(instance);
        if (toKill != null) {
            instance.getDailyActions().put(MafiaActionType.KILL, toKill);
        }
        unpinMessage(instance);
        return brokerHandler.onStart(user, instance);
    }

    @Override
    protected String getChoiceKey() {
        return "GoonHandler.Choices";
    }

    @Override
    protected MafiaState getState() {
        return MafiaState.NIGHT_GOON;
    }
}
