package com.jd.tech.stack.study.serverdemo.app.sku.converter;

import com.jd.tech.stack.study.serverdemo.client.sku.dto.ParamItem;
import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuDTO;
import com.jd.tech.stack.study.serverdemo.domain.sku.bo.ParamItemBO;
import com.jd.tech.stack.study.serverdemo.domain.sku.bo.SkuBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 商品BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class SkuBOConverter {

    public static SkuDTO convert(SkuBO bo) {
        if (bo == null) {
            return null;
        }
        SkuDTO dto = new SkuDTO();
        dto.setSkuId(bo.getSkuId());
        dto.setSkuName(bo.getSkuName());
        dto.setMainImage(bo.getMainImage());
        dto.setPrice(bo.getPrice());
        dto.setParams(convertParamList(bo.getParams()));
        return dto;
    }

    public static ParamItem convertParam(ParamItemBO bo) {
        if (bo == null) {
            return null;
        }
        ParamItem dto = new ParamItem();
        dto.setName(bo.getName());
        dto.setValue(bo.getValue());
        return dto;
    }

    public static List<ParamItem> convertParamList(List<ParamItemBO> boList) {
        if (boList == null) {
            return null;
        }
        List<ParamItem> dtoList = new ArrayList<>();
        for (ParamItemBO bo : boList) {
            dtoList.add(convertParam(bo));
        }
        return dtoList;
    }

    public static List<SkuDTO> convertList(List<SkuBO> boList) {
        if (boList == null) {
            return null;
        }
        List<SkuDTO> dtoList = new ArrayList<>();
        for (SkuBO bo : boList) {
            dtoList.add(convert(bo));
        }
        return dtoList;
    }
}