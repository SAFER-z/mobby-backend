package com.safer.safer.batch.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BatchConstant {
    public static final String REMOVAL_REGEX = "\\(.*?\\)";
    public static final String NUMBER_REGEX = ".*\\d.*";

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final Charset EUC_KR = Charset.forName("euc-kr");
    public static final String STATION = "역";
    public static final String LINE = "선";
    public static final String NUMBER_LINE = "호선";
    public static final String SEOUL = "서울특별시";
}
