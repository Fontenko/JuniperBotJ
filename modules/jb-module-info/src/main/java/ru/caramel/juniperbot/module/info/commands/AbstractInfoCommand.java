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
package ru.caramel.juniperbot.module.info.commands;

import net.dv8tion.jda.core.entities.MessageEmbed;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.caramel.juniperbot.core.model.AbstractCommand;
import ru.caramel.juniperbot.core.service.ContextService;

public abstract class AbstractInfoCommand extends AbstractCommand {

    protected MessageEmbed.Field getDateField(long epochSecond, String nameKey, DateTimeFormatter formatter) {
        DateTime dateTime = new DateTime(epochSecond * 1000).withZone(DateTimeZone.UTC);
        return new MessageEmbed.Field(messageService.getMessage(nameKey),
                String.format("**%s**", formatter.print(dateTime)), true);
    }
}
