package com.home.traininfo.external.ns;

public record Product(
        String number,
        String categoryCode,
        String shortCategoryName,
        String longCategoryName,
        String operatorCode,
        String operatorName,
        Integer operatorAdministrativeCode,
        String type,
        String displayName) {}

