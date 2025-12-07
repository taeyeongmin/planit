package com.square.planit.holiday.dto.res;

import com.square.planit.holiday.dto.HolidayModifyReq;
import lombok.Data;

@Data
public class HolidayDeleteRes{
    String countryCode;
    int year;
    int deletedCount;

    public HolidayDeleteRes(HolidayModifyReq holidayModifyReq, int deletedCount) {
        this.countryCode = holidayModifyReq.getCountryCode();
        this.year = holidayModifyReq.getYear();
        this.deletedCount = deletedCount;
    }
}


