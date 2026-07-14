package com.jd.tech.stack.study.serverdemo.domain.item.converter;

import com.jd.tech.stack.study.serverdemo.domain.item.bo.ItemBO;
import com.jd.tech.stack.study.serverdemo.infra.item.dataobject.ItemDO;

public class ItemDOConverter {

    public static ItemBO convert(ItemDO itemDO) {
        // convert DO to BO
        if (itemDO == null) {
            return null;
        } else {
            return new ItemBO(itemDO.getName());
        }
    }

}
