package com.epam.esm.model.dao;

import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.Tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.model.dao.DaoConstant.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class GiftCertificateHandler {

    private GiftCertificateHandler() {
    }

    public static Map<String, Object> defineChanges(GiftCertificate giftCertificate, GiftCertificate certificateDB) {
        Map<String, Object> result = new HashMap<>();
        if (isStringChanged(certificateDB.getName(), giftCertificate.getName())) {
            result.put(NAME, giftCertificate.getName());
        }
        if (isStringChanged(certificateDB.getDescription(), giftCertificate.getDescription())) {
            result.put(DESCRIPTION, giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null &&
                certificateDB.getPrice().compareTo(giftCertificate.getPrice()) != 0) {
            result.put("price", giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != null &&
                !certificateDB.getDuration().equals(giftCertificate.getDuration())) {
            result.put("duration", giftCertificate.getDuration());
        }
        if (giftCertificate.getCreateDate() != null &&
                !certificateDB.getCreateDate().equals(giftCertificate.getCreateDate())) {
            result.put(CREATE_DATE, giftCertificate.getCreateDate());
        }
        if (giftCertificate.getLastUpdateDate() != null &&
                !certificateDB.getLastUpdateDate().equals(giftCertificate.getLastUpdateDate())) {
            result.put("lastUpdateDate", giftCertificate.getLastUpdateDate());
        }
        return result;
    }

    public static boolean isNotEqualsList(List<Tag> tagsNew, List<Tag> tagsDB) {
        if (tagsNew.size() != tagsDB.size()) {
            return true;
        }
        Collections.sort(tagsNew);
        Collections.sort(tagsDB);
        return !tagsNew.equals(tagsDB);
    }

    private static boolean isStringChanged(String dbField, String field) {
        return isNotEmpty(field) && dbField.equals(field);
    }
}
