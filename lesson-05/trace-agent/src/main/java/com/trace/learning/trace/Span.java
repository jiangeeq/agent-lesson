package com.trace.learning.trace;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * @author jiangpeng
 * @date 2019/12/9 0009
 */
@Data
@NoArgsConstructor
public class Span {
    /**链路ID*/
    private String linkId;
    /**方法进入时间*/
    private Date enterTime;

    public Span(String linkId){
        this.linkId = linkId;
        this.enterTime = new Date();
    }
}
