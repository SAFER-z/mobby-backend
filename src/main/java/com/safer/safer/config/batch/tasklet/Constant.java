package com.safer.safer.config.batch.tasklet;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constant {
    public static final String REMOVAL_REGEX = "\\(.*?\\)";
    public static final String NUMBER_REGEX = ".*\\d.*";

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final Charset EUC_KR = Charset.forName("euc-kr");
    public static final String STATION = "역";
    public static final String LINE = "선";
    public static final String NUMBER_LINE = "호선";
    public static final String SEOUL = "서울특별시";
}
