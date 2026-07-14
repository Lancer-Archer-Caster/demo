package com.jd.tech.stack.study.serverdemo.app.item.converter;

import com.jd.tech.stack.study.serverdemo.client.item.dto.ItemDTO;
import com.jd.tech.stack.study.serverdemo.domain.item.bo.ItemBO;

public class ItemBOConverter {

    public static ItemDTO convert(ItemBO itemBO) {
        // convert BO to DTO
        if (itemBO == null)
           return null;
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setResp(itemBO.getName());
        return itemDTO;
    }

}
