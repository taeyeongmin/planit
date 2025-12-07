package com.square.planit.holiday.controller;

import com.square.planit.holiday.dto.HolidayModifyReq;
import com.square.planit.holiday.dto.HolidaySearchReq;
import com.square.planit.holiday.dto.HolidaySearchRes;
import com.square.planit.holiday.dto.res.HolidayDeleteRes;
import com.square.planit.holiday.enums.HolidayType;
import com.square.planit.holiday.service.HolidayQueryService;
import com.square.planit.holiday.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holiday")
public class HolidayApiController {

    private final HolidayService holidayService;
    private final HolidayQueryService holidayQueryService;

    @Operation(
            summary = "공휴일 최신화 API(Upsert)",
            description = """
                    ## 기능 설명
                    - 국가코드 & 연도를 기준으로 외부 API(공휴일 조회)를 호출하여 데이터 최신화
                    - 기존 공휴일 수정 시 update, 신규 공휴일 추가 시 insert
                    ---
                    ## 검증 조건
                    - 존재하지 않는 국가코드일 경우 예외
                    ---
                    """
    )
    @PostMapping("/refresh")
    public void refreshHolidays(@Valid @RequestBody HolidayModifyReq holidayModifyReq) {
        holidayService.upsertHoliday(holidayModifyReq);
    }

    @Operation(
            summary = "공휴일 삭제",
            description = """
                    ## 기능 설명
                    - 특정 국가와 연도의 모든 공휴일을 삭제.
                    ---
                    ## 특징
                    - 존재하지 않는 국가&연도에 대해 삭제 요청 시 에러 없이 deleteCount=0건으로 성공 처리.
                    ---
                    ## 검증 조건
                    - 존재하지 않는 국가코드일 경우 예외
                    """
    )
    @DeleteMapping
    public ResponseEntity<HolidayDeleteRes> deleteByCountryAndYear(@Valid @RequestBody HolidayModifyReq holidayModifyReq) {
        HolidayDeleteRes result = holidayService.deleteByCountryAndYear(holidayModifyReq);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "공휴일 검색",
            description = """
                    ## 기능 설명
                    - 국가, 연도, 기간, 공휴일 타입 등 다양한 조건으로 Holiday 검색.
                    - `Pageable` 파라미터로 페이지네이션 제공.
                    ---
                    ## 파라미터 설명
                    - countryCode : 국가코드
                    - year        : 연도
                    - from        : 검색 시작일 (YYYY-MM-DD)
                    - to          : 검색 종료일 (YYYY-MM-DD)
                    - types       : 공휴일 종류(Public/Observance/School 등)
                    ---
                    ## 페이지네이션 관련 파라미터
                    - page  : 0부터 시작하는 페이지 index (기본값 0)
                    - size  : 페이지 당 데이터 개수 (기본값 20)
                    - sort  : 정렬 기준, ex) `sort=date,asc`, 여러개 가능
                    ---
                    ## 응답 특징
                    - 페이징 구조로 응답.
                    """
    )
    @Parameters({
            @Parameter(name = "countryCode", description = "국가코드", required = true),
            @Parameter(name = "year", description = "연도" , required = true),
            @Parameter(name = "from", description = "검색 시작일 (yyyy-MM-dd)"),
            @Parameter(name = "to", description = "검색 종료일 (yyyy-MM-dd)"),
            @Parameter(
                    name = "types",
                    description = "공휴일 타입 ,ex) types=PUBLIC,OBSERVANCE",
                    in = ParameterIn.QUERY,
                    array = @ArraySchema(
                            schema = @Schema(
                                    type = "string",
                                    implementation = HolidayType.class
                            )
                    ),
                    explode = Explode.TRUE
            ),
            @Parameter(name = "page", schema = @Schema(type="integer", defaultValue="0")),
            @Parameter(name = "size", schema = @Schema(type="integer", defaultValue="20")),
            @Parameter(name = "sort", description = "정렬 방식")
    })
    @GetMapping("/search")
    public Page<HolidaySearchRes> search(@Parameter(hidden = true) HolidaySearchReq req, @Parameter(hidden = true)Pageable pageable) {
        return holidayQueryService.getHolidaysByScope(req, pageable);
    }
}
