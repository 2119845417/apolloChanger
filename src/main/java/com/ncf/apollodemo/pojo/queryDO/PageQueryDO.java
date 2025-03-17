package com.ncf.apollodemo.pojo.queryDO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryDO {
    private int page;
    private int size;
}
