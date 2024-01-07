package com.littlepants.attack.attackplus.repository;

import com.littlepants.attack.attackplus.entity.graph.Domain;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

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

public interface DomainRepository extends Neo4jRepository<Domain,String> {
    /**
     * 查找所有含有资产的域
     * @return
     */
    @Query("MATCH (domain:Domain)<-[belong:BELONG]-(asset:Asset) RETURN domain")
    List<Domain> getDomainsByAsset();

    @Query("MATCH (domain:Domain)<-[r:BELONG]-(a:Asset{ip:$ip} RETURN domain)")
    List<Domain> getDomainsByAsset(String ip);

    @Query("MATCH (d:Domain) RETURN d")
    List<Domain> getDomains();

    @Query("MATCH (d:Domain{name:$name}) DETACH DELETE d")
    void deleteDomainByName(String name);

}
