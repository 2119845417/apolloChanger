package com.ncf.apollodemo.controller;


import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ncf.apollodemo.resp.ResponseResult;
import com.ncf.apollodemo.service.ApolloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "apolloMain")
public class ApolloMainController {
    private static final Logger logger = LoggerFactory.getLogger(ApolloMainController.class);

    @Autowired
    private ApolloService apolloService;

    //todo
//    @GetMapping(value = "/{env}/getOwnerKey")
//    public ResponseResult<List<OpenItemDTO>> getOwnerKeyByAppId(@PathVariable String env, @RequestBody Integer appId) {
//        apolloService.getOwnerByAppId();
//    }

}
