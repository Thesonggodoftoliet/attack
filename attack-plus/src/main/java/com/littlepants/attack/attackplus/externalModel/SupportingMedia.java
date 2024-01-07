package com.littlepants.attack.attackplus.externalModel;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/12/2
 * @description 支持描述的媒体数据，例如降价，图表，..（可选）。
 * 与 RFC 2397 类似，每个媒体对象都包含三个主要部分：媒体类型、媒体数据值和一个可选的布尔标志，用于指示媒体数据是否经过 base64 编码。
 */
@Data
public class SupportingMedia {
    @NotEmpty(message = "{supportingMedia.type.notnull}")
    @Size(min = 1,max = 256, message = "{supportingMedia.type.validation}")
    @JsonPropertyDescription(value = "符合 RFC2046 的 IANA 媒体类型，例如 text/markdown、text/html。")
    private String type;
    private boolean base64;
    @NotEmpty(message = "{supportingMedia.value.notnull}")
    @Size(min = 1,max = 16384,message = "{supportingMedia.value.validation}")
    private String value;
}
