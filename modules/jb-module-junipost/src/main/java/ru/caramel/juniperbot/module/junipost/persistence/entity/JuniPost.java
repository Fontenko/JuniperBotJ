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
package ru.caramel.juniperbot.module.junipost.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.caramel.juniperbot.core.persistence.entity.GuildOwnedEntity;
import ru.caramel.juniperbot.core.persistence.entity.WebHook;

import javax.persistence.*;

@Entity
@Table(name = "junipost")
@ToString
@Getter
@Setter
public class JuniPost extends GuildOwnedEntity {
    private static final long serialVersionUID = -3872054410668142201L;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "web_hook_id")
    private WebHook webHook;

}
