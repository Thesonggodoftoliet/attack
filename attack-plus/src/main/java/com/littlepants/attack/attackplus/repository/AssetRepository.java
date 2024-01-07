package com.littlepants.attack.attackplus.repository;

import com.littlepants.attack.attackplus.entity.graph.Asset;
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
public interface AssetRepository extends Neo4jRepository<Asset,Long> {
    // 在装载时，必须具备关系也就是 r

    /**
     * 查找图中“存在关系边”、“部署关系边”相连的所有节点
     * MATCH (a:Asset) MATCH (a)-[r:EXIST]->(v:Vulnerability) MATCH (a)-[d:DEPLOY]->(s:Service) RETURN a
     * @return Asset
     */
    @Query("MATCH (a:Asset)-[r:EXIST|DEPLOY]-(e:Vulnerability|Service) RETURN a,collect(r),collect(e)")
    List<Asset> getAll();

    @Query("LOAD CSV WITH HEADERS FROM $path AS line " +
            "MATCH (a:Asset{id:line.begin}) " +
            "MATCH (b:Asset{id:line.end}) " +
            "CREATE (a)-[r:COMMUNICATE]->(b)")
    void saveEdgesFromCSV(@Param("path") String path);

    /**
     * 查找所有存在漏洞的资产
     * MATCH(a:Asset) MATCH(a)-[r:EXIST]->(v:Vulnerability) RETURN a
     * @return Asset
     */
    @Query("MATCH(a:Asset)-[r:EXIST]->(v:Vulnerability) RETURN a")
    List<Asset> getAssetsExistVulnerability();

    /**
     * 查找所有部署了服务的资产
     * MATCH(a:Asset) MATCH (a)-[d:DEPLOY]->(s:Service) RETURN a
     */
    @Query("MATCH(a:Asset)-[d:DEPLOY]->(s:Service) RETURN a")
    List<Asset> getAssetDeployService();


    /**
     * 通过漏洞ID查询存在该漏洞的所有资产
     * MATCH (a:Asset) MATCH(a)-[r:EXIST]->(v:Vulnerability) WHERE v.cve_id = $cveId RETURN collect(a)
     * @return Asset
     */
    @Query("MATCH (a:Asset)-[r:EXIST]->(v:Vulnerability) WHERE v.cve_id = $cveId RETURN a")
    List<Asset> getAssetExistVulByVulId(@Param("cveId") String cveId);

    /**
     * 通过服务ID查询部署了该服务的所有资产
     * MATCH (a:Asset) MATCH (a)-[d:DEPLOY]->(s:Service) where s.id = "1" RETURN a
     */
    @Query("MATCH (a:Asset)-[r:DEPLOY]->(s:Service{name:$name})")
    List<Asset> getAssetsByServiceId(@Param("name") String name);

    /**
     * 创建Asset和Vulnerability之间的关系
     * @param ip String
     * @param cveId String
     * @param pluginId string
     */
    @Query("MATCH (a:Asset{ip:$ip}}),(v:Vulnerability{cve_id:$cve_id,plugin_id:$plugin_id}) " +
            "CREATE (a)-[r:EXIST]->(v)")
    void createExistRelation(@Param("ip") String ip,@Param("cve_id") String cveId,@Param("plugin_id")String pluginId);

    @Query("MATCH (a:Asset{ip:$ip}),(v:Vulnerability{plugin_id:$plugin_id}) " +
            "CREATE (a)-[r:EXIST]->(v)")
    void createExistRelation(@Param("ip") String ip,@Param("plugin_id") String pluginId);

    /**
     * 创建Asset和Service之间的关系
     * @param ip String
     * @param name String
     */
    @Query("MATCH (a:Asset{ip:$ip}),(s:Service{name:$name}) " +
            "CREATE (a)-[r:DEPLOY]->(s)")
    void createDeployRelation(@Param("ip") String ip,@Param("name") String name);

    @Query("MATCH (a:Asset{ip:$ip}),(s:Service{plugin_id:$plugin_id}) "+
            "CREATE (a)-[r:DEPLOY{version:$version,ports:$ports}]->(s)")
    void createDeployRelation(@Param("ip") String ip,@Param("plugin_id") String pluginId,
                              @Param("version") String version,@Param("ports")List<String> ports);

    /**
     * 创建Asset和Asset之间的关系
     * @param ip1 String
     * @param ip2 String
     */
    @Query("MATCH (a:Asset{ip:$ip1}),(b:Asset{ip:$ip2}) " +
            "CREATE (a)-[:COMMUNICATE]->(b)")
    void createCommunicateRelation(@Param("ip1") String ip1,@Param("ip2") String ip2);

    /**
     * 创建Asset和Domain之间的关系
     * @param ip String
     * @param name String
     */
    @Query("MATCH (a:Asset{ip:$ip}),(d:Domain{name:$name}) " +
            "CREATE (a)-[:BELONG]->(d)")
    void createBelongRelation(@Param("ip") String ip,@Param("name") String name);

    /**
     * 通过IP更新资产名称
     * @param ip String
     * @param name String
     */
    @Query("MATCH (a:Asset{ip:$ip}) SET a.name=$name, a.paw=$paw")
    void updateAssetNameByIp(@Param("ip")String ip,@Param("name")String name,@Param("paw")String paw);

    /**
     * 通过IP更新资产类型
     * @param ip String
     * @param assetType String
     */
    @Query("MATCH (a:Asset{ip:$ip}) SET a.asset_type=$asset_type")
    void updateAssetTypeByIp(@Param("ip")String ip,@Param("asset_type")String assetType);

    /**
     * 删除该ip的所有关系以及自身
     * @param ip String ip
     */
    @Query("MATCH (a:Asset{ip:$ip) DETACH DELETE a")
    void deleteAllRelationOfAsset(@Param("ip") String ip);

    @Query("MATCH (a:Asset{ip:$ip}-[r:EXIST]->(v:Vulnerability{cve_id:$cve_id})) DELETE r")
    void deleteVulRelation(@Param("ip") String ip,@Param("cve_id") String cve_id);

    @Query("MATCH (a:Asset{ip:$ip}-[r:DEPLOY]->(s:Service{name:$name})) DELETE r")
    void deleteServiceRelation(@Param("ip") String ip,@Param("name") String name);

    @Query("MATCH (a:Asset{ip:$ip}-[:BELONG]->(d:Domain{name:$name})) DELETE r")
    void deleteDomainRelation(@Param("ip") String ip,@Param("name") String name);

}
