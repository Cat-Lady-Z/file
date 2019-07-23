package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.entity.ManageResult;
import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.SuccessManageResult;
import com.lbx.distribution.manageserver.util.JsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: 商户、单位/公司、门店公共service
 * @Description:
 */
@Service
public class ManageCommonService {

    /**
     * 获取响应参数
     */
    public String getRespBody(Object o) {
        ManageResult result = new SuccessManageResult<>(o);
        ResponseEntity<ManageResult> responseEntity = ResponseEntity.ok(result);

        String resp = null;
        try {
            resp = JsonUtils.objectToJson(responseEntity.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp == null ? "" : resp.trim();
    }


    public Integer getPageNum(Map<String, Object> manageSimpleRequest) {
        Object pageNum = manageSimpleRequest.get("pageNum");

        if (pageNum != null){
            if (pageNum instanceof Integer){
                Integer pageSize_int = (Integer) pageNum;
                return pageSize_int;
            }
        }

        return null;
    }

    public Integer getPageSize(Map<String, Object> manageSimpleRequest) {
        Object pageSize = manageSimpleRequest.get("pageSize");

        if (pageSize != null){
            if (pageSize instanceof Integer){
                Integer pageSize_int = (Integer) pageSize;
                return pageSize_int;
            }
        }

        return null;
    }

    /**
     * 设置菜单的层级type
     * @param menuResults
     * @param type
     * @return
     */
    public List<MenuEntity> setType(List<MenuEntity> menuResults, Integer type) {
        if (menuResults == null)
            return null;

        List<MenuEntity> temp = new ArrayList<>();

        for (MenuEntity me:menuResults ) {

            me.setType(type);

            temp.add(me);
        }

        return temp;
    }
}
