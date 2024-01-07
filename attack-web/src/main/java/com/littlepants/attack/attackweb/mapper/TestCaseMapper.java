package com.littlepants.attack.attackweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackweb.entity.TestCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface TestCaseMapper extends BaseMapper<TestCase> {
    @Select("SELECT COUNT(*) FROM attack.testcase WHERE campaign_id = #{campaignId}")
    int getCountOfSameCampaign(@Param("campaignId") String campaignId);

    @Select("SELECT COUNT(*) FROM testcase WHERE campaign_id = #{campaignId} AND " +
            "JSON_UNQUOTE(json_extract(redteam,'$.status')) = '3'")
    int getCountOfFinished(@Param("campaignId")String campaignId);

    @Select("SELECT COUNT(*) FROM testcase WHERE campaign_id = #{campaignId} AND "+
            "JSON_UNQUOTE(json_extract(blueteam,'$.outcome')) = '0'")
    int getCountOfTBD(@Param("campaignId")String campaignId);

    //#{}会自动添加单引号、${}会将你的值原封不动的插入
    @Select("<script>" +
            "SELECT COUNT(*) FROM attack.testcase WHERE (json_extract(redteam,'$.phase')= #{phase}) AND " +
            "(json_extract(blueteam,'$.outcome')='1' OR " +
            "json_extract(blueteam,'$.outcome')='2') AND " +
            "campaign_id IN " +
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            "</script>"
    )
    int getCountOfSuccessByPhase(@Param("phase") String phaseId, @Param("campaignIds")List<String> campaignIds);

    @Select("<script>" +
            "SELECT COUNT(*) FROM attack.testcase WHERE (json_extract(redteam,'$.technique')= #{technique}) AND " +
            "(json_extract(blueteam,'$.outcome')='1' OR " +
            "json_extract(blueteam,'$.outcome')='2') AND " +
            "campaign_id IN " +
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            "</script>"
    )
    int getCountOfSuccessByTechnique(@Param("technique") String techniqueId, @Param("campaignIds")List<String> campaignIds);

    @Select("<script>"+
            "SELECT COUNT(*) AS num, YEARWEEK(create_time) AS btime  FROM attack.testcase " +
            "WHERE campaign_id IN "+
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            "AND DATE(create_time) BETWEEN #{beginTime} AND #{endTime} GROUP BY btime ORDER BY btime DESC"+
            "</script>"
    )
    List<Map<String,Object>> getWeeklyCount(@Param("campaignIds")List<String> campaignIds,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime);

    @Select("<script>"+
            "SELECT COUNT(*) AS num, DATE_FORMAT(create_time,'%Y-%m') AS btime FROM attack.testcase " +
            "WHERE campaign_id IN "+
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            "AND DATE(create_time) BETWEEN #{beginTime} AND #{endTime} GROUP BY btime ORDER BY btime DESC"+
            "</script>"
    )
    List<Map<String,Object>> getMonthlyCount(@Param("campaignIds")List<String> campaignIds,
                                             @Param("beginTime")Date beginTime,
                                             @Param("endTime")Date endTime);

    @Select("<script>"+
            "SELECT COUNT(*) AS num, QUARTER(create_time) AS btime,YEAR(create_time) AS byear FROM attack.testcase " +
            "WHERE campaign_id IN "+
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            "AND DATE(create_time) BETWEEN #{beginTime} AND #{endTime} GROUP BY btime,byear ORDER BY byear DESC,btime DESC"+
            "</script>"
    )
    List<Map<String,Object>> getQuarterlyCount(@Param("campaignIds")List<String> campaignIds,
                                               @Param("beginTime")Date beginTime,
                                               @Param("endTime")Date endTime);

    @Select("<script>"+
            "SELECT COUNT(*) AS num, DATE_FORMAT(create_time,'%Y') AS btime FROM attack.testcase " +
            "WHERE campaign_id IN "+
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            " AND DATE(create_time) BETWEEN #{beginTime} AND #{endTime} GROUP BY btime ORDER BY btime DESC"+
            "</script>"
    )
    List<Map<String,Object>> getYearlyCount(@Param("campaignIds")List<String> campaignIds,
                                            @Param("beginTime")Date beginTime,
                                            @Param("endTime")Date endTime);
    @Select("<script>"+
            "SELECT JSON_UNQUOTE(json_extract(redteam,'$.phase')) as phaseId,COUNT(*) FROM attack.testcase WHERE campaign_id IN "+
            "<foreach item='campaignId' index='index' collection='campaignIds' open='(' separator=',' close=')'>"+
            "#{campaignId}"+
            "</foreach>"+
            " AND JSON_UNQUOTE(json_extract(blueteam,'$.outcome'))=#{outcome} GROUP BY phaseId"+
            "</script>"
    )
    List<Map<String,Object>> getPhaseStatic(@Param("campaignIds")List<String> campaignIds,@Param("outcome") int outcome);
}
