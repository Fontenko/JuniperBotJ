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
package ru.caramel.juniperbot.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ConfigDto implements Serializable {

    private static final long serialVersionUID = 5706403671642660929L;

    @Size(max = 20, message = "{validation.config.prefix.Size.message}")
    @NotBlank(message = "{validation.config.prefix.NotBlank.message}")
    @Pattern(regexp = "[^\\s]+", message = "{validation.config.prefix.Pattern.message}")
    private String prefix;

    @Size(max = 10)
    @NotBlank
    private String locale;

    @NotNull
    private Boolean privateHelp;

    @Valid
    private MusicConfigDto musicConfig;

    @Valid
    private ModerationConfigDto modConfig;

    @Valid
    private WebHookDto webHook;

    @Valid
    private List<VkConnectionDto> vkConnections;
}
