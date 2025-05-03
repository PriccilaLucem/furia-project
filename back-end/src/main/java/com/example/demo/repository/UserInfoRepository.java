package com.example.demo.repository;

import com.example.demo.model.UserInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoModel, UUID>, JpaSpecificationExecutor<UserInfoModel> {

    @Query("SELECT DISTINCT u FROM UserInfoModel u " +
    "LEFT JOIN FETCH u.address a " +
    "LEFT JOIN FETCH u.interaction i " +
    "WHERE " +
    "(:fansScoreMin IS NULL OR u.fansScore IS NULL OR u.fansScore >= :fansScoreMin) AND " +
    "(:fansScoreMax IS NULL OR u.fansScore IS NULL OR u.fansScore <= :fansScoreMax) AND " +
    "(:city IS NULL OR a IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
    "(:alreadyWentToFuriaEvent IS NULL OR i IS NULL OR i.alreadyWentToFuriaEvent = :alreadyWentToFuriaEvent) AND " +
    "(:boughtItems IS NULL OR i IS NULL OR i.boughtItems = :boughtItems) AND " +
    "(:eFuriaClubMember IS NULL OR i IS NULL OR i.eFuriaClubMember = :eFuriaClubMember)")

    List<UserInfoModel> findWithFilters(
        @Param("fansScoreMin") Integer fansScoreMin,
        @Param("fansScoreMax") Integer fansScoreMax,
        @Param("city") String city,
        @Param("alreadyWentToFuriaEvent") Boolean alreadyWentToFuriaEvent,
        @Param("boughtItems") Boolean boughtItems,
        @Param("eFuriaClubMember") Boolean eFuriaClubMember
    );

    @Query("SELECT u FROM UserInfoModel u " +
           "LEFT JOIN FETCH u.address " +
           "LEFT JOIN FETCH u.interaction " +
           "WHERE u.email = :email")
    Optional<UserInfoModel> findByEmail(@Param("email") String email);
}