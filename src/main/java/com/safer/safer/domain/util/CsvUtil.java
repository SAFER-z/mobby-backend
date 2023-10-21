package com.safer.safer.domain.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.safer.safer.exception.FileIOException;

import java.io.*;
import java.util.List;

import static com.safer.safer.exception.ExceptionCode.*;

public class CsvUtil {
    public static <T> List<T> readCsv(String filePath, Class<T> clazz) throws Exception {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withSkipLines(1)
                .build()) {

            return new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .build()
                    .parse();

        } catch (FileNotFoundException e) {
            throw new FileIOException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new FileIOException(FAIL_TO_READ_FILE);
        }
    }
}