package com.littlepants.attack.attackplus.repository;

import com.littlepants.attack.attackplus.entity.graph.Service;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/27
 */
@Repository
public interface ServiceRepository extends Neo4jRepository<Service,Long> {

    /**
     *通过资产IP查找这个资产上部署了哪些服务
     */
    @Query("MATCH (s:Service)<-[r:DEPLOY]-(a:Asset{ip:$ip}) RETURN s")
    List<Service> getServicesByAsset(@Param("ip") String ip);

    @Query("MATCH (s:Service) RETURN s")
    List<Service> getServices();

    @Query("MATCH (s:Service)<-[r:DEPLOY]-(a:Asset) RETURN s")
    List<Service> getServicesByAsset();

    @Query("MATCH (s:Service) WHERE s.id=$id DETACH DELETE s")
    void deleteServiceById(@Param("id") Long id);

    @Query("MATCH (s:Service{id:$id})<-[r:DEPLOY]-(a:Asset) DELETE r")
    void deleteRelationById(@Param("id") Long id);

}
