package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description 一组结构（称为容器），用于存储与参与 CVE 计划的特定组织提供的特定 CVE ID 相关的漏洞信息。每个容器都包含不同来源提供的信息。
 * 至少，必须包含一个“cna”容器，其中包含最初分配 CVE ID 的 CNA 提供的漏洞信息。
 * 只能有一个“cna”容器，因为只能有一个分配 CNA。但是，可以有多个“adp”容器，允许多个参与 CVE 计划的组织添加与漏洞相关的额外信息。
 * 大多数情况下，“cna”和“adp”容器包含相同的属性。主要区别在于信息来源。 'cna' 容器要求 CNA 包含某些字段，而 'adp' 容器则不需要。
 */
@Data
public class Containers {
}
