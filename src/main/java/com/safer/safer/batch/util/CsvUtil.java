package com.safer.safer.batch.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.exception.FileIOException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.*;
import static com.safer.safer.common.exception.ExceptionCode.*;

public class CsvUtil {
    public static <T> List<T> readCsv(String filePath, Charset encoding, Class<T> clazz) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
        try (CSVReader reader = new CSVReaderBuilder(br)
                .withSkipLines(1)
                .build()) {

            return new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .build()
                    .parse();

        } catch (FileNotFoundException e) {
            throw new FileIOException(FILE_NOT_FOUND, filePath);
        } catch (IOException e) {
            throw new FileIOException(FAIL_TO_READ_FILE, filePath);
        }
    }

    public static String parseStationName(String stationName) {
        stationName = parseParenthesis(stationName);
        if(!stationName.endsWith(STATION))
            stationName = stationName.concat(STATION);

        return stationName;
    }

    public static String generateNameByStation(Station station, FacilityType type) {
        return String.join(" ", station.getName(), type.getName());
    }

    public static String generateNameByStation(Station station, String number, FacilityType type) {
        return String.join(" ", station.getName(), number.concat("번"), type.getName());
    }

    public static String parseLine(String line) {
        if(line.matches(NUMBER_REGEX) && !line.endsWith(NUMBER_LINE)) {
            line = line.concat(NUMBER_LINE);
        }
        if(!line.endsWith(LINE))
            line = line.concat(LINE);

        return line;
    }

    public static String parseParenthesis(String input) {
        return input.replaceAll(REMOVAL_REGEX, "").trim();
    }

    public static String parseTime(String input) {
        if(input.equals("0"))
            return "00:00";
        if(input.length() < 4)
            input = "0"+input;

        String hours = input.substring(0, 2);
        String minutes = input.substring(2);
        return hours + ":" + minutes;
    }
}