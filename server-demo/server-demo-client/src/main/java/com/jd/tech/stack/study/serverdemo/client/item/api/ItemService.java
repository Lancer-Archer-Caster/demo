package com.jd.tech.stack.study.serverdemo.client.item.api;

import com.jd.tech.stack.study.serverdemo.client.item.param.ItemParam;
import com.jd.tech.stack.study.serverdemo.client.item.dto.ItemDTO;

/**
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ItemService {
    ItemDTO sayHello(ItemParam param);
}
