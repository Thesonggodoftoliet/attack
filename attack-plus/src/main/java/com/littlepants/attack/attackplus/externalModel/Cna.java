package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description 包含 CVE 编号机构 (CNA) 为已发布的 CVE ID 提供的漏洞信息的对象。
 * 每个 CVE 记录只能有一个 CNA 容器，因为只能有一个分配 CNA。 CNA 容器必须包含 CVE 规则中定义的必需信息，其中包括产品、版本、问题类型、描述和参考。
 */
@Data
public class Cna {
    private ProviderMetadata providerMetadata;
    private String dateAssigned;
    private String datePublic;
    private String title;
    private List<ProblemType> problemTypes;
    private List<Impact> impacts;
    private List<Affected> affected;
    private List<CnaDescription> descriptions;
    private List<Metric> metrics;
    private List<CnaDescription> solutions;
    private List<CnaDescription> workarounds;
    private List<CnaDescription> configurations;
    private List<CnaDescription> exploits;
    private List<Timeline> timeline;
    private List<Credit> credits;
    private List<Reference> references;
    private List<String> tags;
    private Map<String,String> source;
    private List<TaxonomyMapping> taxonomyMappings;


}
