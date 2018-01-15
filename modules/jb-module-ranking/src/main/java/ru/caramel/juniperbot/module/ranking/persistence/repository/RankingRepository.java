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
package ru.caramel.juniperbot.module.ranking.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.caramel.juniperbot.core.persistence.entity.LocalMember;
import ru.caramel.juniperbot.module.ranking.persistence.entity.Ranking;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findByMemberGuildIdOrderByExpDesc(String guildId);

    @Query("SELECT r FROM Ranking r WHERE r.member = (SELECT m FROM LocalMember m WHERE m.guildId = :guildId AND m.user.userId = :userId)")
    Ranking findByGuildIdAndUserId(@Param("guildId") String guildId, @Param("userId") String userId);

    Ranking findByMember(LocalMember member);

    @Modifying
    @Query("UPDATE Ranking r SET r.exp = 0 WHERE r.member = :member")
    int resetMember(@Param("member") LocalMember member);

    @Modifying
    @Query("UPDATE Ranking r SET r.exp = 0 WHERE r.member IN (SELECT m FROM LocalMember m WHERE m.guildId = :guildId)")
    int resetAll(@Param("guildId") String guildId);
}