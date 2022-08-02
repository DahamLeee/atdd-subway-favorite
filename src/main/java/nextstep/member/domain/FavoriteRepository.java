package nextstep.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f where f.member.id = :memberId")
    List<Favorite> findFavoritesByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from Favorite f where f.member.id = :memberId and f.id = :favoriteId")
    void deleteMemberFavoriteById(@Param("memberId") Long memberId, @Param("favoriteId") Long favoriteId);
}
