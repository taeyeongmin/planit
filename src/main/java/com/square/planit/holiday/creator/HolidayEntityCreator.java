package com.square.planit.holiday.creator;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.holiday.dto.HolidayKey;
import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.entity.Holiday;
import com.square.planit.holiday.entity.HolidayScope;
import com.square.planit.holiday.enums.HolidayType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holiday Entity 생성용
 */
@Component
public class HolidayEntityCreator {

    /**
     * 전달 받은 공휴일 API 데이터를 Entity로 변환
     * @param country
     * @param apiRows
     * @return
     */
    public List<Holiday> createHoliday(Country country, List<HolidayRes> apiRows) {

        // HolidayKey 기준으로 그룹핑
        Map<HolidayKey, List<HolidayRes>> grouped =
                apiRows.stream()
                        .collect(Collectors.groupingBy(hr ->
                                HolidayKey.of(country, hr.date(), hr.localName(), hr.name())
                        ));

        List<Holiday> result = new ArrayList<>();


        for (Map.Entry<HolidayKey, List<HolidayRes>> entry : grouped.entrySet()) {

            List<HolidayRes> rows = entry.getValue();

            HolidayRes base = rows.getFirst();

            Holiday holiday = Holiday.create(country, base);

            // scope 생성
            for (HolidayRes hr : rows) {

                Set<String> counties = new LinkedHashSet<>();
                if (hr.counties() != null) {
                    counties.addAll(hr.counties());
                }

                Set<HolidayType> types = new LinkedHashSet<>();
                if (hr.types() != null) {
                    for (String typeStr : hr.types()) {
                        types.add(HolidayType.of(typeStr));
                    }
                }

                HolidayScope scope = new HolidayScope(counties, types);
                holiday.addScope(scope);
            }

            result.add(holiday);
        }

        return result;
    }

}
