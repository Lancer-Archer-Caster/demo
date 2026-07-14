package com.jd.tech.stack.study.serverdemo.app.compliance.converter;

import com.jd.tech.stack.study.serverdemo.client.compliance.dto.ComplianceHit;
import com.jd.tech.stack.study.serverdemo.client.compliance.dto.ComplianceResultDTO;
import com.jd.tech.stack.study.serverdemo.domain.compliance.bo.ComplianceHitBO;
import com.jd.tech.stack.study.serverdemo.domain.compliance.bo.ComplianceResultBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 合规BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ComplianceBOConverter {

    public static ComplianceResultDTO convert(ComplianceResultBO bo) {
        if (bo == null) {
            return null;
        }
        ComplianceResultDTO dto = new ComplianceResultDTO();
        dto.setPassed(bo.getPassed());
        dto.setHits(convertHitList(bo.getHits()));
        List<String> suggestions = new ArrayList<>();
        if (bo.getHits() != null) {
            for (ComplianceHitBO hit : bo.getHits()) {
                if (hit.getSuggestion() != null) {
                    suggestions.add(hit.getSuggestion());
                }
            }
        }
        dto.setSuggestions(suggestions);
        return dto;
    }

    public static ComplianceHit convertHit(ComplianceHitBO bo) {
        if (bo == null) {
            return null;
        }
        ComplianceHit dto = new ComplianceHit();
        dto.setWord(bo.getHitContent());
        dto.setCategory(bo.getCategory());
        dto.setPosition(null);
        return dto;
    }

    public static List<ComplianceHit> convertHitList(List<ComplianceHitBO> boList) {
        if (boList == null) {
            return null;
        }
        List<ComplianceHit> dtoList = new ArrayList<>();
        for (ComplianceHitBO bo : boList) {
            dtoList.add(convertHit(bo));
        }
        return dtoList;
    }
}
