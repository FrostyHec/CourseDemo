package org.frosty.object_storage.controller;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstant.API+"/storage")
@RequiredArgsConstructor
public class PublicAccessController {

    public void getFileFromPublic(String accessKey){

    }
}
