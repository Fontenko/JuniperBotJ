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
package ru.caramel.juniperbot.module.audio.commands.timing;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import ru.caramel.juniperbot.core.model.DiscordCommand;
import ru.caramel.juniperbot.core.utils.CommonUtils;
import ru.caramel.juniperbot.module.audio.model.PlaybackInstance;
import ru.caramel.juniperbot.module.audio.model.TrackRequest;

@DiscordCommand(
        key = "discord.command.rewind.key",
        description = "discord.command.rewind.desc",
        group = "discord.command.group.music",
        source = ChannelType.TEXT,
        priority = 113)
public class RewindCommand extends TimingCommand {

    @Override
    protected boolean doInternal(MessageReceivedEvent message, TrackRequest request, long millis) {
        PlaybackInstance instance = playerService.getInstance(message.getGuild());
        AudioTrack track = request.getTrack();
        long position = instance.getPosition();
        millis = Math.min(position, millis);
        if (playerService.getInstance(message.getGuild()).seek(position - millis)) {
            messageManager.onMessage(message.getChannel(), "discord.command.audio.rewind",
                    messageManager.getTitle(track.getInfo()), CommonUtils.formatDuration(millis));
            request.setResetMessage(true);
            return true;
        }
        return false;
    }
}
